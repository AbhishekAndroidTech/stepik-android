package org.stepik.android.presentation.auth

import io.reactivex.Scheduler
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.stepic.droid.analytic.Analytic
import org.stepik.android.domain.auth.model.LoginFailType
import org.stepic.droid.di.qualifiers.BackgroundScheduler
import org.stepic.droid.di.qualifiers.MainScheduler
import org.stepic.droid.model.Credentials
import org.stepik.android.domain.auth.interactor.AuthInteractor
import org.stepik.android.presentation.base.PresenterBase
import retrofit2.HttpException
import javax.inject.Inject

class CredentialAuthPresenter
@Inject
constructor(
    private val analytic: Analytic,
    private val authInteractor: AuthInteractor,

    @BackgroundScheduler
    private val backgroundScheduler: Scheduler,
    @MainScheduler
    private val mainScheduler: Scheduler
) : PresenterBase<CredentialAuthView>() {
    private var state: CredentialAuthView.State = CredentialAuthView.State.Idle
        set(value) {
            field = value
            view?.setState(value)
        }

    override fun attachView(view: CredentialAuthView) {
        super.attachView(view)
        view.setState(state)
    }

    fun onFormChanged() {
        state = CredentialAuthView.State.Idle
    }

    fun submit(credentials: Credentials, isRegistration: Boolean = false) {
        if (state == CredentialAuthView.State.Loading ||
            state is CredentialAuthView.State.Success) {
            return
        }

        state = CredentialAuthView.State.Loading
        compositeDisposable += authInteractor
            .authWithCredentials(credentials, isRegistration)
            .subscribeOn(backgroundScheduler)
            .observeOn(mainScheduler)
            .subscribeBy(
                onSuccess = { state = CredentialAuthView.State.Success(it) },
                onError = { throwable ->
                    val loginFailType =
                        if (throwable is HttpException) {
                            if (throwable.code() == 429) {
                                LoginFailType.TOO_MANY_ATTEMPTS
                            } else {
                                LoginFailType.EMAIL_PASSWORD_INVALID
                            }
                        } else {
                            LoginFailType.CONNECTION_PROBLEM
                        }

                    if (throwable is HttpException) {
                        analytic.reportEvent(Analytic.Error.CREDENTIAL_AUTH_FAILED, throwable.response()?.errorBody()?.string() ?: "empty response")
                    } else {
                        analytic.reportError(Analytic.Error.CREDENTIAL_AUTH_FAILED, throwable)
                    }

                    state = CredentialAuthView.State.Error(loginFailType)
                }
            )
    }
}