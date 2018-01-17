package org.stepic.droid.adaptive.model

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.stepic.droid.base.App
import org.stepic.droid.di.qualifiers.BackgroundScheduler
import org.stepic.droid.di.qualifiers.MainScheduler
import org.stepic.droid.model.Attempt
import org.stepic.droid.model.Lesson
import org.stepic.droid.model.Step
import org.stepic.droid.model.Unit
import org.stepic.droid.web.Api
import org.stepic.droid.web.ViewAssignment
import javax.inject.Inject

class Card(
        private val courseId: Long,
        val lessonId: Long,

        var lesson: Lesson? = null,
        var step: Step? = null,
        var attempt: Attempt? = null
) : Single<Card>() {
    @Inject
    lateinit var api: Api

    @Inject
    @field:MainScheduler
    lateinit var mainScheduler: Scheduler

    @Inject
    @field:BackgroundScheduler
    lateinit var backgroundScheduler: Scheduler

    init {
        App.componentManager()
                .adaptiveCourseComponent(courseId)
                .inject(this)
    }

    private var observer: SingleObserver<in Card>? = null

    private var error: Throwable? = null

    private var lessonDisposable: Disposable? = null
    private var stepSubscription: Disposable? = null
    private var attemptDisposable: Disposable? = null

    private val compositeDisposable = CompositeDisposable()

    var correct = false
        private set

    fun initCard() {
        error = null

        if (stepSubscription == null || stepSubscription?.isDisposed == true && step == null) {
            stepSubscription = api.getStepsByLessonId(lessonId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ setStep(it.steps?.firstOrNull()) }, { onError(it) })
        } else {
            setStep(step)
        }

        if (lessonDisposable == null || lessonDisposable?.isDisposed == true && lesson == null) {
            lessonDisposable = api.getLessons(lessonId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ setLesson(it.lessons?.firstOrNull()) }, { onError(it) })
        }
    }

    private fun setStep(newStep: Step?) = newStep?.let {
        this.step = newStep
        if (attemptDisposable == null || attemptDisposable?.isDisposed == true && attempt == null) {
            attemptDisposable = Observable.concat(
                    api.getExistingAttemptsReactive(newStep.id).toObservable(),
                    api.createNewAttemptReactive(newStep.id).toObservable()
            )
                    .filter { it.attempts.isNotEmpty() }
                    .take(1)
                    .map { it.attempts.firstOrNull() }
                    .subscribeOn(backgroundScheduler)
                    .observeOn(mainScheduler)
                    .subscribe({ setAttempt(it) }, { onError(it) })
        }

        compositeDisposable.add(api.getUnits(courseId, lessonId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ reportView(it.units?.firstOrNull(), newStep.id) }, {}))
        notifyDataChanged()
    }

    private fun reportView(unit: Unit?, stepId: Long) = unit?.assignments?.firstOrNull().let { assignmentId ->
        compositeDisposable.add(api.postViewedReactive(ViewAssignment(assignmentId, stepId))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({}, {}))
    }

    private fun setLesson(lesson: Lesson?) = lesson?.let {
        this.lesson = it
        notifyDataChanged()
    }

    private fun setAttempt(attempt: Attempt?) = attempt?.let {
        this.attempt = it
        notifyDataChanged()
    }

    private fun onError(error: Throwable?) {
        this.error = error
        notifyDataChanged()
    }

    private fun notifyDataChanged() = observer?.let {
        error?.let(it::onError)

        if (lesson != null && step != null && attempt != null) {
            it.onSuccess(this)
        }
    }

    /**
     * Free resources
     */
    fun recycle() {
        lessonDisposable?.dispose()
        stepSubscription?.dispose()
        attemptDisposable?.dispose()
        compositeDisposable.dispose()
        observer = null
    }

    override fun subscribeActual(observer: SingleObserver<in Card>) {
        this.observer = observer
        initCard()
        notifyDataChanged()
    }

    fun onCorrect() {
        correct = true
    }
}