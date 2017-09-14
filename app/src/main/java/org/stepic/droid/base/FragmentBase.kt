package org.stepic.droid.base

import android.app.DownloadManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import butterknife.ButterKnife
import butterknife.Unbinder
import org.stepic.droid.R
import org.stepic.droid.analytic.Analytic
import org.stepic.droid.concurrency.MainHandler
import org.stepic.droid.configuration.Config
import org.stepic.droid.core.*
import org.stepic.droid.fonts.FontsProvider
import org.stepic.droid.notifications.LocalReminder
import org.stepic.droid.preferences.SharedPreferenceHelper
import org.stepic.droid.preferences.UserPreferences
import org.stepic.droid.storage.CancelSniffer
import org.stepic.droid.storage.IDownloadManager
import org.stepic.droid.storage.operations.DatabaseFacade
import org.stepic.droid.ui.activities.contracts.BottomNavigationViewRoot
import org.stepic.droid.ui.util.CloseIconHolder
import org.stepic.droid.util.resolvers.CoursePropertyResolver
import org.stepic.droid.util.resolvers.text.TextResolver
import org.stepic.droid.web.Api
import java.util.concurrent.ThreadPoolExecutor
import javax.inject.Inject

open class FragmentBase : Fragment() {

    private var viewHasBeenDestroyed = false
    private var unbinder: Unbinder? = null

    @Inject
    lateinit var localReminder: LocalReminder

    @Inject
    lateinit var textResolver: TextResolver

    @Inject
    lateinit var shareHelper: ShareHelper

    @Inject
    lateinit var analytic: Analytic

    @Inject
    lateinit var threadPoolExecutor: ThreadPoolExecutor

    @Inject
    lateinit var databaseFacade: DatabaseFacade

    @Inject
    lateinit var fontsProvider: FontsProvider

    @Inject
    lateinit var config: Config

    @Inject
    lateinit var api: Api

    @Inject
    lateinit var screenManager: ScreenManager

    @Inject
    lateinit var localProgressManager: LocalProgressManager

    @Inject
    lateinit var downloadManager: IDownloadManager

    @Inject
    lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    @Inject
    lateinit var userPreferences: UserPreferences

    @Inject
    lateinit var coursePropertyResolver: CoursePropertyResolver

    @Inject
    lateinit var mainHandler: MainHandler

    @Inject
    lateinit var audioFocusHelper: AudioFocusHelper

    @Inject
    lateinit var systemDownloadManager: DownloadManager

    @Inject
    lateinit var cancelSniffer: CancelSniffer

    @Inject
    lateinit var exoPhoneListener: MyExoPhoneStateListener

    protected open fun injectComponent() {
        App.component().inject(this)
    }

    /**
     * optional method for releasing components
     * mirror of `injectComponent()`
     */
    protected open fun onReleaseComponent() {}

    protected fun hideSoftKeypad() {
        val view = this.activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isAcceptingText) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        injectComponent()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (unbinder != null) {
            //in Kotlin, for example, butterknife is not used
            unbinder?.unbind()
        }
        viewHasBeenDestroyed = true
    }

    override fun onDestroy() {
        super.onDestroy()
        onReleaseComponent()
        //        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        //        refWatcher.watch(this);
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val shouldNotBeenDestroyed = viewHasBeenDestroyed && enter
        viewHasBeenDestroyed = false

        //do not animate fragment on rotation via setCustomAnimations
        return if (shouldNotBeenDestroyed)
            object : Animation() {}
        else
            super.onCreateAnimation(transit, enter, nextAnim)
    }

    @DrawableRes
    protected fun getCloseIconDrawableRes(): Int =
            CloseIconHolder.getCloseIconDrawableRes()

    /**
     * Background of activity can be nullified for avoiding overdrawing.
     *
     * Background of fragment's root view should be initialized in fragment or xml
     *
     * Colors in theme and fragments without background is not used, because toolbars and other element (not
     * root) will be always overdrawn.
     *
     * Null is not used at Theme because of black background on pre-loading activity.
     */
    protected fun nullifyActivityBackground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity?.window?.decorView?.background = null
        } else {
            @Suppress("DEPRECATION")
            activity?.window?.decorView?.setBackgroundDrawable(null)
        }
    }

    /**
     * Apply margin if activity has bottom navigation bar
     */
    protected fun applyBottomMarginForRootView() {
        activity as? BottomNavigationViewRoot ?: return
        val rootView: ViewGroup = getRootView() ?: throw IllegalStateException("For using applyBottomMarginForRootView, you should override getRootView() in your fragment.")

        val layoutParams = rootView.layoutParams as ViewGroup.MarginLayoutParams

        layoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.bottom_navigation_height)
    }

    protected open fun getRootView(): ViewGroup? = null
}