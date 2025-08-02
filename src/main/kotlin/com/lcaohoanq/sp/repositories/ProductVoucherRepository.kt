package com.lcaohoanq.sp.repositories

import com.lcaohoanq.sp.domains.discount.ProductVoucher
import org.springframework.data.jpa.repository.JpaRepository

interface ProductVoucherRepository: JpaRepository<ProductVoucher, Long> {

    fun findByProductId(productId: Long): List<ProductVoucher>

    fun findByVoucherId(voucherId: Long): List<ProductVoucher>

    fun findByProductIdAndVoucherId(productId: Long?, voucherId: Long?): ProductVoucher?

}