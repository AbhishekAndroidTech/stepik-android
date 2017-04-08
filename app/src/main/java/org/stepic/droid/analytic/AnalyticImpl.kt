package org.stepic.droid.analytic

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.yandex.metrica.YandexMetrica
import org.json.JSONObject
import org.stepic.droid.configuration.Config
import org.stepic.droid.di.AppSingleton
import java.util.*
import javax.inject.Inject

@AppSingleton
class AnalyticImpl
@Inject constructor(context: Context, config: Config) : Analytic {
    override fun reportEventValue(eventName: String, value: Long) {
        val bundle = Bundle()
        bundle.putLong(FirebaseAnalytics.Param.VALUE, value)
        reportEvent(eventName, bundle)
    }

    override fun setUserId(userId: String) {
        firebaseAnalytics.setUserId(userId)
    }

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    private val mixPanelAnalytics: MixpanelAPI = MixpanelAPI.getInstance(context, config.mixpanelToken);

    override fun reportEvent(eventName: String, bundle: Bundle?) {
        val map: HashMap<String, String> = HashMap()
        bundle?.keySet()?.forEach {
            map.put(it, bundle.get(it).toString())
        }
        if (map.isEmpty()) {
            YandexMetrica.reportEvent(eventName)
            mixPanelAnalytics.track(eventName)
        } else {
            YandexMetrica.reportEvent(eventName, map as Map<String, Any>?)
            val jsonObject = JSONObject(map)
            mixPanelAnalytics.track(eventName, jsonObject)
        }

        val eventNameLocal = castStringToFirebaseEvent(eventName)
        firebaseAnalytics.logEvent(eventNameLocal, bundle)
    }

    override fun reportEvent(eventName: String) {
        reportEvent(eventName, null)
    }

    override fun reportError(message: String, throwable: Throwable) {
        FirebaseCrash.report(throwable)
        YandexMetrica.reportError(message, throwable)
        mixPanelAnalytics.track(message)
    }

    override fun reportEvent(eventName: String, id: String) {
        reportEventWithIdName(eventName, id, null)
    }

    override fun reportEventWithName(eventName: String, name: String?) {
        val bundle = Bundle()
        if (name != null) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        }
        reportEvent(eventName, bundle)
    }

    override fun reportEventWithIdName(eventName: String, id: String, name: String?) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        if (name != null) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        }
        reportEvent(eventName, bundle)
    }

    private fun castStringToFirebaseEvent(eventName: String): String {
        var eventNameLocal =
                if (eventName == Analytic.Interaction.SUCCESS_LOGIN) {
                    FirebaseAnalytics.Event.LOGIN
                } else {
                    eventName
                }

        val sb = StringBuilder()
        eventNameLocal.forEach {
            if (Character.isLetterOrDigit(it) && !Character.isWhitespace(it)) {
                sb.append(it)
            } else {
                sb.append("_")
            }
        }
        eventNameLocal = sb.toString()

        if (eventNameLocal.length > 32L) {
            eventNameLocal = eventNameLocal.substring(0, 32)
        }
        return eventNameLocal
    }
}
