package org.stepik.android.view.profile_links.ui.delegate

import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import org.stepic.droid.R
import org.stepic.droid.ui.util.setCompoundDrawables
import org.stepik.android.model.SocialProfile
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class ProfileLinksAdapterDelegate(
    private val onItemClick: (String) -> Unit
) : AdapterDelegate<SocialProfile, DelegateViewHolder<SocialProfile>>() {
    companion object {
        private const val FACEBOOK = "facebook"
        private const val INSTAGRAM = "instagram"
        private const val TWITTER = "twitter"
        private const val VK = "vk"
    }

    override fun isForViewType(position: Int, data: SocialProfile): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<SocialProfile> =
        ViewHolder(createView(parent, R.layout.item_profile_link))

    private inner class ViewHolder(
        root: View
    ) : DelegateViewHolder<SocialProfile>(root) {

        private val profileLinkTextView = root as AppCompatTextView

        init {
            root.setOnClickListener { onItemClick(itemData?.url ?: "") }
        }

        override fun onBind(data: SocialProfile) {
            profileLinkTextView.movementMethod = LinkMovementMethod.getInstance()
            profileLinkTextView.text = data.name
            profileLinkTextView.setCompoundDrawables(start = resolveProfileLinkDrawable(data.provider))
        }
    }

    private fun resolveProfileLinkDrawable(provider: String): Int =
        when (provider) {
            FACEBOOK ->
                R.drawable.ic_profile_fb
            INSTAGRAM ->
                R.drawable.ic_profile_instagram
            TWITTER ->
                R.drawable.ic_profile_twitter
            VK ->
                R.drawable.ic_profile_vk
            else ->
                R.drawable.ic_profile_web
        }
}