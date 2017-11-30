package org.stepic.droid.core

import org.stepic.droid.di.AppCoreComponent
import org.stepic.droid.di.course_general.CourseGeneralComponent
import org.stepic.droid.di.downloads.DownloadsComponent
import org.stepic.droid.di.login.LoginComponent
import org.stepic.droid.di.mainscreen.MainScreenComponent
import org.stepic.droid.di.routing.RoutingComponent
import org.stepic.droid.di.splash.SplashComponent
import org.stepic.droid.di.step.StepComponent
import org.stepic.droid.util.SuppressFBWarnings
import timber.log.Timber

class ComponentManagerImpl(private val appCoreComponent: AppCoreComponent) : ComponentManager {

    // Splash

    private var _splashComponent: SplashComponent? = null

    override fun splashComponent(): SplashComponent {
        if (_splashComponent == null) {
            _splashComponent = appCoreComponent.splashComponent().build()
        }
        return _splashComponent!!
    }

    override fun releaseSplashComponent() {
        _splashComponent = null
    }

    // Downloads

    private val downloadsComponent
            by lazy {
                appCoreComponent
                        .downloadsComponentBuilder()
                        .build()
            }

    override fun downloadsComponent(): DownloadsComponent = downloadsComponent

    // Step

    private val stepComponentMap = HashMap<Long, StepComponent>()
    private val stepComponentCountMap = HashMap<Long, Int>()

    override fun stepComponent(stepId: Long): StepComponent {
        val count = stepComponentCountMap[stepId] ?: 0
        stepComponentCountMap[stepId] = count + 1
        val routingComponent = routingComponent() //increment routing component by invoking method
        return stepComponentMap.getOrPut(stepId) {
            routingComponent
                    .stepComponentBuilder()
                    .build()
        }
    }

    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", justification = "false positive")
    override fun releaseStepComponent(stepId: Long) {
        releaseRoutingComponent()
        val count: Int = stepComponentCountMap[stepId] ?: throw IllegalStateException("release step component, which is not allocated")
        if (count == 1) {
            //it is last
            stepComponentMap.remove(stepId)
        }
        stepComponentCountMap[stepId] = count - 1
    }


    // Login

    private val loginComponentMap = HashMap<String, LoginComponent>()

    override fun releaseLoginComponent(tag: String) {
        loginComponentMap.remove(tag)
    }

    override fun loginComponent(tag: String) =
            loginComponentMap.getOrPut(tag) {
                appCoreComponent
                        .loginComponentBuilder()
                        .build()
            }

    // Main Screen

    private var mainScreenComponentProp: MainScreenComponent? = null

    override fun mainFeedComponent(): MainScreenComponent {
        synchronized(this) {
            if (mainScreenComponentProp == null) {
                mainScreenComponentProp = appCoreComponent
                        .mainScreenComponentBuilder()
                        .build()
            }
            return mainScreenComponentProp!!
        }
    }

    override fun releaseMainFeedComponent() {
        synchronized(this) {
            mainScreenComponentProp = null
        }
    }

    // Routing

    private val routingComponentHolder = ComponentHolder<RoutingComponent>()

    override fun routingComponent(): RoutingComponent {
        return routingComponentHolder.get {
            appCoreComponent
                    .routingComponentBuilder()
                    .build()
        }
    }


    override fun releaseRoutingComponent() {
        routingComponentHolder.release()
    }


    // Course general

    private val _courseGeneralComponent by lazy {
        appCoreComponent
                .courseGeneralComponentBuilder()
                .build()
    }

    override fun courseGeneralComponent(): CourseGeneralComponent = _courseGeneralComponent
}

class ComponentHolder<T> {
    private var refCount = 0
    private var component: T? = null

    fun get(creationBlock: () -> T): T {
        if (component == null) {
            component = creationBlock.invoke()
        }

        refCount++
        Timber.d("$component allocated with refCount = $refCount")
        return component!!
    }

    fun release() {
        refCount--
        if (refCount == 0) {
            component = null
        }

        Timber.d("$component released with new refCount = $refCount")

        if (refCount < 0) {
            throw IllegalStateException("released component greater than got")
        }
    }

}
