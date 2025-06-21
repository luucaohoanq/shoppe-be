package com.lcaohoanq.sp.entities

import BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "order_status_logs")
class OrderStatusLog(): BaseEntity() {

    @Id
    @SequenceGenerator(name = "order_status_logs_seq", sequenceName = "order_status_logs_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_status_logs_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var orderId: Int? = null
    var status: Int? = null

}