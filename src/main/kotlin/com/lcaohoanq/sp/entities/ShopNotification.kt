package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "shop_notifications")
class ShopNotification(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "shop_notifications_seq", sequenceName = "shop_notifications_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shop_notifications_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var shopId: Int? = null
    var type: Int? = null
    var content: String? = null
    var isRead: Boolean = false

}