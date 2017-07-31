package org.stepic.droid.viewmodel

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import org.stepic.droid.R

data class ProfileSettingsViewModel(
        @StringRes val stringRes: Int,
        @ColorRes val textColor: Int = R.color.new_main_color
)