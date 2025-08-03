package com.lcaohoanq.sp.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.domains.user.User
import jakarta.persistence.*


@Entity
@Table(name = "user_device_tokens")
class UserDeviceToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    val user: User,

    @Column(name = "device_id", nullable = false, unique = true)
    val deviceId: String,

    @Column(name = "fcm_token", nullable = false, unique = true, length = 512)
    var fcmToken: String,

    @Column(name = "device_name")
    var deviceName: String? = "Unknown Device",

    @Column(name = "platform")
    var platform: String? = "Unknown Platform",

): BaseEntity() {



}