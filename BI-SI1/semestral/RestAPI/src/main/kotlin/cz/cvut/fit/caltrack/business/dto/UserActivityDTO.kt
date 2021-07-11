package cz.cvut.fit.caltrack.business.dto

import java.sql.Date

data class UserActivityReadDTO (
    override val id: Int,
    val activity: ActivityReadDTO,
    val date: Date,
    val duration: Int
) : IReadDTO

data class UserActivityUpdateDTO (
    val activityId: Int?,
    val date: Date?,
    val duration: Int?
) : IUpdateDTO

data class UserActivityCreateDTO (
    val activityId: Int,
    val date: Date?,
    val duration: Int
) : ICreateDTO