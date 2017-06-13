package org.stepic.droid.di

import android.app.AlarmManager
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.squareup.otto.Bus
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.stepic.droid.BuildConfig
import org.stepic.droid.R
import org.stepic.droid.analytic.Analytic
import org.stepic.droid.analytic.AnalyticImpl
import org.stepic.droid.base.Client
import org.stepic.droid.base.ClientImpl
import org.stepic.droid.base.ListenerContainer
import org.stepic.droid.base.ListenerContainerImpl
import org.stepic.droid.concurrency.MainHandler
import org.stepic.droid.concurrency.MainHandlerImpl
import org.stepic.droid.concurrency.SingleThreadExecutor
import org.stepic.droid.configuration.Config
import org.stepic.droid.configuration.ConfigImpl
import org.stepic.droid.core.*
import org.stepic.droid.core.internet_state.InternetEnabledPosterImpl
import org.stepic.droid.core.internet_state.contract.InternetEnabledListener
import org.stepic.droid.core.internet_state.contract.InternetEnabledPoster
import org.stepic.droid.core.video_moves.VideosMovedPosterImpl
import org.stepic.droid.core.video_moves.contract.VideosMovedListener
import org.stepic.droid.core.video_moves.contract.VideosMovedPoster
import org.stepic.droid.fonts.FontsProvider
import org.stepic.droid.fonts.FontsProviderImpl
import org.stepic.droid.notifications.*
import org.stepic.droid.preferences.SharedPreferenceHelper
import org.stepic.droid.preferences.UserPreferences
import org.stepic.droid.social.SocialManager
import org.stepic.droid.storage.*
import org.stepic.droid.storage.operations.DatabaseFacade
import org.stepic.droid.util.connectivity.NetworkTypeDeterminer
import org.stepic.droid.util.connectivity.NetworkTypeDeterminerImpl
import org.stepic.droid.util.resolvers.VideoResolver
import org.stepic.droid.util.resolvers.VideoResolverImpl
import org.stepic.droid.util.resolvers.text.TextResolver
import org.stepic.droid.util.resolvers.text.TextResolverImpl
import org.stepic.droid.web.Api
import org.stepic.droid.web.ApiImpl
import org.stepic.droid.web.UserAgentProvider
import org.stepic.droid.web.UserAgentProviderImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor


@Module
abstract class AppCoreModule {

    @Binds
    @AppSingleton
    abstract fun provideLocalProgressManagerSectionProgressListenerContainer(container: ListenerContainerImpl<LocalProgressManager.SectionProgressListener>): ListenerContainer<LocalProgressManager.SectionProgressListener>

    @Binds
    @AppSingleton
    abstract fun provideLocalProgressManagerUnitProgressListenerContainer(container: ListenerContainerImpl<LocalProgressManager.UnitProgressListener>): ListenerContainer<LocalProgressManager.UnitProgressListener>

    @Binds
    @AppSingleton
    abstract fun provideStoreStateManagerLessonCallbackContainer(container: ListenerContainerImpl<StoreStateManager.LessonCallback>): ListenerContainer<StoreStateManager.LessonCallback>

    @Binds
    @AppSingleton
    abstract fun provideStoreStateManagerSectionCallbackContainer(container: ListenerContainerImpl<StoreStateManager.SectionCallback>): ListenerContainer<StoreStateManager.SectionCallback>

    @Binds
    @AppSingleton
    abstract fun provideInternetEnabledPoster(internetEnabledPoster: InternetEnabledPosterImpl): InternetEnabledPoster

    @Binds
    @AppSingleton
    abstract fun provideInternetEnabledListenerContainer(container: ListenerContainerImpl<InternetEnabledListener>): ListenerContainer<InternetEnabledListener>

    @Binds
    @AppSingleton
    abstract fun provideInternetEnabledClient(container: ClientImpl<InternetEnabledListener>): Client<InternetEnabledListener>


    @Binds
    @AppSingleton
    abstract fun provideVideoMovedPoster(videosMovedPoster: VideosMovedPosterImpl): VideosMovedPoster

    @Binds
    @AppSingleton
    abstract fun provideVideoMovedListenerContainer(container: ListenerContainerImpl<VideosMovedListener>): ListenerContainer<VideosMovedListener>

    @Binds
    @AppSingleton
    abstract fun provideVideoMovedClient(container: ClientImpl<VideosMovedListener>): Client<VideosMovedListener>

    @Binds
    @AppSingleton
    internal abstract fun provideScreenManager(screenManager: ScreenManagerImpl): ScreenManager

    @Binds
    @AppSingleton
    internal abstract fun provideIApi(api: ApiImpl): Api

    @Binds
    @AppSingleton
    internal abstract fun provideVideoResolver(videoResolver: VideoResolverImpl): VideoResolver

    @Binds
    @AppSingleton
    internal abstract fun provideDownloadManger(downloadManager: DownloadManagerImpl): IDownloadManager

    @AppSingleton
    @Binds
    internal abstract fun provideStoreManager(storeStateManager: StoreStateManagerImpl): StoreStateManager

    @AppSingleton
    @Binds
    internal abstract fun provideCleanManager(cleanManager: CleanManagerImpl): CleanManager

    @AppSingleton
    @Binds
    internal abstract fun provideLessonSessionManager(localLessonSessionManager: LocalLessonSessionManagerImpl): LessonSessionManager

    @AppSingleton
    @Binds
    internal abstract fun provideProgressManager(localProgress: LocalProgressImpl): LocalProgressManager

    @Binds
    @AppSingleton
    internal abstract fun provideCancelSniffer(cancelSniffer: ConcurrentCancelSniffer): CancelSniffer

    @AppSingleton
    @Binds
    internal abstract fun provideHandlerForUIThread(mainHandler: MainHandlerImpl): MainHandler

    @AppSingleton
    @Binds
    internal abstract fun provideLocalReminder(localReminder: LocalReminderImpl): LocalReminder

    @AppSingleton
    @Binds
    internal abstract fun provideNotificationManager(notificationManager: StepikNotificationManagerImpl): StepikNotificationManager

    @Binds
    @AppSingleton
    internal abstract fun provideAnalytic(analytic: AnalyticImpl): Analytic

    @Binds
    @AppSingleton
    internal abstract fun provideShareHelper(shareHelper: ShareHelperImpl): ShareHelper

    @Binds
    @AppSingleton
    internal abstract fun provideDefaultFilter(defaultFilter: DefaultFilterImpl): DefaultFilter

    @Binds
    @AppSingleton
    internal abstract fun provideFilterApplicator(filterApplicator: FilterApplicatorImpl): FilterApplicator

    @Binds
    @AppSingleton
    internal abstract fun provideTextResolver(textResolver: TextResolverImpl): TextResolver

    @Binds
    @AppSingleton
    internal abstract fun provideLessonDownloader(lessonDownloader: LessonDownloaderImpl): LessonDownloader

    @Binds
    @AppSingleton
    internal abstract fun provideSectionDownloader(sectionDownloader: SectionDownloaderImpl): SectionDownloader

    @Binds
    @AppSingleton
    internal abstract fun provideVideoLengthResolver(videoLengthResolver: VideoLengthResolverImpl): VideoLengthResolver

    @Binds
    @AppSingleton
    internal abstract fun provideUserAgent(userAgentProvider: UserAgentProviderImpl): UserAgentProvider

    @Binds
    @AppSingleton
    internal abstract fun provideFontProvider(fontsProvider: FontsProviderImpl): FontsProvider

    @Binds
    abstract fun provideNetworkTypeDeterminer(networkTypeDeterminer: NetworkTypeDeterminerImpl): NetworkTypeDeterminer

    @Module
    companion object {

        @Provides
        @JvmStatic
        @AppSingleton
        internal fun provideSharedPreferencesHelper(analytic: Analytic, defaultFilter: DefaultFilter, context: Context): SharedPreferenceHelper {
            return SharedPreferenceHelper(analytic, defaultFilter, context)
        }

        @Provides
        @AppSingleton
        @JvmStatic
        internal fun provideBus(): Bus {
            return Bus()
        }

        @Provides
        @AppSingleton
        @JvmStatic
        internal fun provideUserPrefs(context: Context, helper: SharedPreferenceHelper, analytic: Analytic): UserPreferences {
            return UserPreferences(context, helper, analytic)
        }

        @Provides
        @JvmStatic
        internal fun provideSystemDownloadManager(context: Context): DownloadManager {
            return context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        }

        @Provides
        @JvmStatic
        internal fun provideSystemAlarmManager(context: Context): AlarmManager {
            return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }

        @AppSingleton
        @Provides
        @JvmStatic
        internal fun provideSocialManager(): SocialManager {
            return SocialManager()
        }

        @Provides
        @JvmStatic
        @AppSingleton
        internal fun provideSingle(): SingleThreadExecutor {
            return SingleThreadExecutor(Executors.newSingleThreadExecutor())
        }

        //it is good for many short lived, which should do async
        @Provides
        @AppSingleton
        @JvmStatic
        internal fun provideThreadPool(): ThreadPoolExecutor {
            return Executors.newCachedThreadPool() as ThreadPoolExecutor
        }

        @Provides
        @JvmStatic
        internal fun provideCommentsManager(): CommentManager {
            return CommentManager()
        }

        /**
         * this retrofit is only for parsing error body
         */
        @Provides
        @AppSingleton
        @JvmStatic
        fun provideRetrofit(config: Config): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(config.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient())
                    .build()
        }

        @Provides
        @JvmStatic
        @AppSingleton
        internal fun provideStepikLogoutManager(threadPoolExecutor: ThreadPoolExecutor, mainHandler: MainHandler, userPreferences: UserPreferences, sharedPreferenceHelper: SharedPreferenceHelper, downloadManager: DownloadManager, dbFacade: DatabaseFacade): StepikLogoutManager {
            return StepikLogoutManager(threadPoolExecutor, mainHandler, userPreferences, downloadManager, sharedPreferenceHelper, dbFacade)
        }

        @Provides
        @JvmStatic
        @AppSingleton
        internal fun provideDownloadUpdaterAfterRestart(threadPoolExecutor: ThreadPoolExecutor,
                                                        systemDownloadManager: DownloadManager,
                                                        storeStateManager: StoreStateManager,
                                                        databaseFacade: DatabaseFacade): InitialDownloadUpdater {
            return InitialDownloadUpdater(threadPoolExecutor, systemDownloadManager, storeStateManager, databaseFacade)
        }

        @Provides
        @JvmStatic
        @AppSingleton
        internal fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
            val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build()
            firebaseRemoteConfig.setConfigSettings(configSettings)
            firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults)
            return firebaseRemoteConfig
        }


        @Provides
        @JvmStatic
        internal fun provideSystemNotificationManager(context: Context) =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        @Provides
        @JvmStatic
        internal fun provideRescheduleChecker(blockNotificationIntervalProvider: BlockNotificationIntervalProvider): NotificationTimeChecker {
            return NotificationTimeCheckerImpl(blockNotificationIntervalProvider.start, blockNotificationIntervalProvider.end)
        }


        @Provides
        @AppSingleton
        @JvmStatic
        internal fun provideBlockNotificationIntervalProvider() = BlockNotificationIntervalProvider()


        @Provides
        @JvmStatic
        internal fun provideConnectivityManager(context: Context): ConnectivityManager {
            return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }


        @Provides
        @AppSingleton
        @JvmStatic
        internal fun provideConfig(configFactory: ConfigImpl.ConfigFactory): Config {
            return configFactory.create()
        }
    }

}
