package com.lcaohoanq.sp.entities

import BaseEntity
import com.lcaohoanq.sp.enums.UserEnum
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserExtra(
    @Id
    @SequenceGenerator(name = "users_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @Column(name = "id", unique = true, nullable = false)
    val id: Int? = null,

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