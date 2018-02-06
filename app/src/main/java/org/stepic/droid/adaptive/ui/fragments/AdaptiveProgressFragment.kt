package org.stepic.droid.adaptive.ui.fragments

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.stepic.droid.R
import org.stepic.droid.adaptive.ui.adapters.AdaptiveWeeksAdapter
import org.stepic.droid.base.App
import org.stepic.droid.base.FragmentBase
import org.stepic.droid.core.presenters.AdaptiveProgressPresenter
import org.stepic.droid.core.presenters.contracts.AdaptiveProgressView
import org.stepic.droid.util.AppConstants
import javax.inject.Inject

class AdaptiveProgressFragment : FragmentBase(), AdaptiveProgressView {
    companion object {
        fun newInstance(courseId: Long) = AdaptiveProgressFragment().apply {
            arguments = Bundle(1).apply { putLong(AppConstants.KEY_COURSE_LONG_ID, courseId) }
        }
    }

    private var courseId = 0L

    @Inject
    lateinit var adaptiveProgressPresenter: AdaptiveProgressPresenter

    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        courseId = arguments.getLong(AppConstants.KEY_COURSE_LONG_ID, 0)
        super.onCreate(savedInstanceState)
    }

    override fun injectComponent() {
        App.componentManager()
                .adaptiveCourseComponent(courseId)
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        recycler = RecyclerView(context)
        recycler.layoutManager = LinearLayoutManager(context)

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.list_divider_h))
        recycler.addItemDecoration(divider)

        return recycler
    }

    override fun onWeeksAdapter(adapter: AdaptiveWeeksAdapter) {
        recycler.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adaptiveProgressPresenter.attachView(this)
    }

    override fun onStop() {
        adaptiveProgressPresenter.detachView(this)
        super.onStop()
    }

    override fun onReleaseComponent() {
        App.componentManager()
                .releaseAdaptiveCourseComponent(courseId)
    }

    override fun onDestroy() {
        adaptiveProgressPresenter.destroy()
        super.onDestroy()
    }
}