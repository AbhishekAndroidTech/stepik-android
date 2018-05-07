package org.stepic.droid.ui.quiz

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import org.stepic.droid.model.Attempt
import org.stepic.droid.model.Reply
import org.stepic.droid.model.Submission

abstract class QuizDelegate {
    abstract var isEnabled: Boolean
    abstract var actionButton: Button?

    abstract fun onCreateView(parent: ViewGroup): View
    protected open fun onViewCreated(view: View) {}
    fun createView(parent: ViewGroup) =
        onCreateView(parent).also { onViewCreated(it) }

    abstract fun setAttempt(attempt: Attempt?)
    abstract fun setSubmission(submission: Submission?)
    abstract fun createReply(): Reply
}