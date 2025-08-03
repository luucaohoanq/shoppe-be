package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.user.UserExtra
import org.springframework.data.jpa.repository.JpaRepository

interface UserExtraRepository: JpaRepository<UserExtra, Long> {
}