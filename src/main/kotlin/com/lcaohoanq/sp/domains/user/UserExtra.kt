package com.lcaohoanq.sp.domains.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.lcaohoanq.sp.bases.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_extra")
class UserExtra(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    @OneToOne(mappedBy = "userExtra")
    @JsonIgnore
    val user: User? = null,

    @Column(name = "avatar")
    val avatar: String = "https://api.dicebear.com/9.x/adventurer/svg?seed=${user?.email ?: "default"}",

    @Column(name = "date_of_birth")
    val dateOfBirth: String? = "",
): BaseEntity() {


}