package cz.cvut.fit.caltrack.business.service

import cz.cvut.fit.caltrack.business.dto.*
import cz.cvut.fit.caltrack.data.entity.*
import cz.cvut.fit.caltrack.data.repository.*
import org.springframework.stereotype.Service
import java.sql.Date

@Service
class UserActivityService (override val repository: UserActivityRepository, val userRepository: UserRepository, val activityRepository: ActivityRepository)
    : IServiceImpl<UserActivity, UserActivityReadDTO, UserActivityCreateDTO, UserActivityUpdateDTO>(repository) {

    /**
     *  Finds all activities user has ever performed, ordered by frequency
     *  @param user user who has performed the activities
     *  @return list of ActivityReadDTOs of activities given user performed
     */
    fun getHistory (user: UserReadDTO) : List<ActivityReadDTO>
        = repository.getHistory(userRepository.getOne(user.id)).take(IService.PAGE_SIZE).map { it.toDTO() }

    /**
     *  Finds all activities user performed on given day
     *  @param user user who has performed the activities
     *  @param day day on which the activity was performed
     *  @return list of UserActivityReadDTOs of activities given user performed on given day
     */
    fun getDaily   (user: UserReadDTO, day: Date) : List<UserActivityReadDTO>
        = repository.getDaily(userRepository.getOne(user.id), day).map { it.toDTO() }

    /**
     * Helper extension function to convert Activity to ActivityReadDTO
     * @return ActivityReadDTO reflecting the Activity
     */
    fun Activity.toDTO() = ActivityReadDTO(id, name, caloriesPerMinute, unit, unitToMinutes)

    // === INTERFACE METHOD IMPLEMENTATION ===
    override fun UserActivity.toDTO() = UserActivityReadDTO (id, activity.toDTO(), date, duration)
    override fun UserActivityCreateDTO.toEntity(user: UserReadDTO) = UserActivity (
        userRepository.getOne(user.id),
        activityRepository.getOne(activityId),
        date ?: Date(System.currentTimeMillis()),
        duration
    )
    override fun UserActivity.merge (dto: UserActivityUpdateDTO, user: UserReadDTO) = UserActivity (
        userRepository.getOne(user.id),
        dto.activityId?.let { activityRepository.getOne(it) } ?: activity,
        dto.date ?: date,
        dto.duration ?: duration, id
    )
    // === INTERFACE METHOD IMPLEMENTATION ===

}