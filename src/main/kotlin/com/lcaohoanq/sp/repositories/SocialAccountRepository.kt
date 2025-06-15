package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.user.SocialAccount
import org.springframework.data.jpa.repository.JpaRepository

interface SocialAccountRepository : JpaRepository<SocialAccount, Long>
