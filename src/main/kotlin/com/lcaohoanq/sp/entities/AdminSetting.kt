package com.lcaohoanq.sp.entities

import com.lcaohoanq.sp.bases.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "admin_settings")
class AdminSetting(): BaseEntity() {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Int? = null

    var settingKey: String? = null
    var settingValue: String? = null

}