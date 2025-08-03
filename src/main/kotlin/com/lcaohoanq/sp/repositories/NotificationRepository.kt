package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.entities.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<NotificationEntity, Long> {
    fun findByUserEmail(email: String): MutableList<NotificationEntity>
    fun findByUserEmailOrderByCreatedAtDesc(email: String): MutableList<NotificationEntity>
}
