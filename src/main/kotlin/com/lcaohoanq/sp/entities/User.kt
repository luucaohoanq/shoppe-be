package com.lcaohoanq.sp.entities

import BaseEntity
import com.lcaohoanq.sp.enums.UserEnum
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserExtra(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "email", unique = true)
    val email: String,

    @Column(name = "full_name", unique = false)
    var fullName: String = "New User",

    @Column(name = "phone", unique = true)
    val phone: String = "",


    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role", nullable = false)
    var role: UserEnum.Role? = UserEnum.Role.CUSTOMER,

): BaseEntity(){

}