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

    @Column(name = "setting_key")
    var settingKey: String? = null
    @Column(name = "setting_value")
    var settingValue: String? = null

}