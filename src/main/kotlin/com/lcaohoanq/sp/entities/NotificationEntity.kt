package com.lcaohoanq.sp.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.domains.user.User
import com.lcaohoanq.sp.enums.NotificationEnum
import jakarta.persistence.*

@Entity
@Table(name = "notifications")
class NotificationEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    val type: NotificationEnum.NotificationType,

    @Enumerated(EnumType.STRING)
    val iconName: NotificationEnum.NotificationIcon,

    @Enumerated(EnumType.STRING)
    val iconColorHex: NotificationEnum.NotificationColor,

    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,


    @Column(name = "read", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    var read: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    val user: User? = null,



    ): BaseEntity() {
}