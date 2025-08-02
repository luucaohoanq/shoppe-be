package com.lcaohoanq.sp.domains.discount

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface VoucherService {

    fun getAll(pageable: Pageable): Page<Voucher>

    fun getVoucherById(id: Long): Voucher

    fun createVoucher(voucher: Voucher): Voucher

    fun updateVoucher(id: Long, voucher: Voucher): Voucher?

    fun deleteVoucher(id: Long)

    fun getAllProductVouchers(): List<ProductVoucher>

    fun getProductVoucherById(id: Long): ProductVoucher

    fun createProductVoucher(productVoucher: ProductVoucher): ProductVoucher

    fun updateProductVoucher(id: Long, productVoucher: ProductVoucher): ProductVoucher?

    fun deleteProductVoucher(id: Long)

    fun getVouchersByProductId(productId: Long): List<Voucher>

}