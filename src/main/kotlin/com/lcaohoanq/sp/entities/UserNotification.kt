package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_notifications")
class UserNotification(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "user_notifications_seq", sequenceName = "user_notifications_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_notifications_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var userId: Int? = null
    var type: Int? = null
    var content: String? = null
    var isRead: Boolean = false

}