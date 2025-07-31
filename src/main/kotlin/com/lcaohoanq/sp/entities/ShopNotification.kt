package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "shop_notifications")
class ShopNotification(): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    var shopId: Int? = null
    var type: Int? = null
    var content: String? = null
    var isRead: Boolean = false

}