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
@Table(name = "admin_settings")
class AdminSetting(): BaseEntity() {


    @Id
    @SequenceGenerator(name = "admin_settings_seq", sequenceName = "admin_settings_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_settings_seq")
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null

    var key: String? = null
    var value: String? = null

}