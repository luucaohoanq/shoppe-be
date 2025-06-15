package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.loginhistory.LoginHistory
import org.springframework.data.jpa.repository.JpaRepository

interface LoginHistoryRepository : JpaRepository<LoginHistory, Long>{
    fun findTop1ByUserIdOrderByLoginAtDesc(userId: Long): LoginHistory?
    fun findTop5ByUserIdOrderByLoginAtDesc(userId: Long): MutableList<LoginHistory>

    fun existsByUserId(userId: Long): Boolean
}
