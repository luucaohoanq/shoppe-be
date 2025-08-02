package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.discount.Voucher
import org.springframework.data.jpa.repository.JpaRepository

interface VoucherRepository: JpaRepository<Voucher, Long> {
}