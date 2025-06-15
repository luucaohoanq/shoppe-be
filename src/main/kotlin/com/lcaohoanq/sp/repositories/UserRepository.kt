package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface UserRepository: JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    fun findByUserName(userName: String): Optional<User>
    fun existsByEmail(email: String): Boolean
    fun findByEmailAndHashedPassword(email: String, password: String): User?
    fun findByEmail(email: String): User?
}
