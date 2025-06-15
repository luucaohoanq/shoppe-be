package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.user.Address
import com.lcaohoanq.sp.domains.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository: JpaRepository<Address, Long> {
    fun user(user: User): MutableList<Address>
}
