package org.stepic.droid.util.resolvers.text

import android.content.Context
import android.text.SpannableString
import android.text.style.URLSpan
import org.stepic.droid.fonts.FontType
import org.stepic.droid.fonts.FontsProvider
import org.stepic.droid.ui.custom.CustomFontURLSpan
import uk.co.chrisjenx.calligraphy.TypefaceUtils
import javax.inject.Inject

class NotificationTextResolver
@Inject
constructor(
        private val fontsProvider: FontsProvider,
        private val textResolver: TextResolver
) {

    fun resolveNotificationText(context: Context, content: String?): CharSequence {
        val spanned = SpannableString(textResolver.fromHtml(content))
        val typeface = TypefaceUtils.load(context.assets, fontsProvider.provideFontPath(FontType.medium))
        spanned.getSpans(0, spanned.length, URLSpan::class.java).forEach {
            val start = spanned.getSpanStart(it)
            val end = spanned.getSpanEnd(it)
            val flags = spanned.getSpanFlags(it)
            spanned.removeSpan(it)
            spanned.setSpan(CustomFontURLSpan(it.url, typeface), start, end, flags)
        }
        return spanned
    }

}