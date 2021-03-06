package org.stepik.android.domain.assignment.repository

import io.reactivex.Maybe
import io.reactivex.Single
import org.stepik.android.domain.base.DataSourceType
import org.stepik.android.model.Assignment

interface AssignmentRepository {
    fun getAssignments(assignmentIds: List<Long>, sourceType: DataSourceType = DataSourceType.CACHE): Single<List<Assignment>>

    /**
     * Lookups assignment in cache
     */
    fun getAssignmentByUnitAndStep(
        unitId: Long,
        stepId: Long
    ): Maybe<Assignment>
}