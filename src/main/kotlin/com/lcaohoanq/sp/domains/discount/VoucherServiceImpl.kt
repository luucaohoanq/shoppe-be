package com.lcaohoanq.sp.domains.discount

import com.lcaohoanq.sp.domains.product.ProductService
import com.lcaohoanq.sp.repositories.ProductVoucherRepository
import com.lcaohoanq.sp.repositories.VoucherRepository
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class VoucherServiceImpl(
    private val voucherRepository: VoucherRepository,
    private val productVoucherRepository: ProductVoucherRepository,
    private val productService: ProductService
) : VoucherService {

    private val log = KotlinLogging.logger { }

    override fun getAll(pageable: Pageable): Page<Voucher> {
        return voucherRepository.findAll(pageable)
    }

    override fun getVoucherById(id: Long): Voucher {
        return voucherRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Voucher with ID $id does not exist.") }
    }

    override fun createVoucher(voucher: Voucher): Voucher {
        return voucherRepository.save(voucher)
    }

    override fun updateVoucher(
        id: Long,
        voucher: Voucher
    ): Voucher? {
        TODO("Not yet implemented")
    }

    override fun deleteVoucher(id: Long) {
        val voucher = voucherRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Voucher with ID $id does not exist.") }
        voucher.expired = true
        voucherRepository.save(voucher)
    }

    override fun getAllProductVouchers(): List<ProductVoucher> {
        return productVoucherRepository.findAll().sortedBy { it.id }
    }

    override fun getProductVoucherById(id: Long): ProductVoucher {
        return productVoucherRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Product Voucher with ID $id does not exist.") }
    }

    override fun createProductVoucher(productVoucher: ProductVoucher): ProductVoucher {
        // Check if the product exists
        if (!productService.existsById(productVoucher.product?.id ?: -1)) {
            throw IllegalArgumentException("Product with ID ${productVoucher.product?.id} does not exist.")
        }

        // Check if the voucher exists
        if (voucherRepository.findById(productVoucher.voucher?.id ?: -1).isEmpty) {
            throw IllegalArgumentException("Voucher with ID ${productVoucher.voucher?.id} does not exist.")
        }

        // Check for existing product-voucher relationship
        val existingProductVoucher = productVoucherRepository.findByProductIdAndVoucherId(
            productVoucher.product?.id,
            productVoucher.voucher?.id
        )
        if (existingProductVoucher != null) {
            throw IllegalArgumentException("Product Voucher already exists for Product ID ${productVoucher.product?.id} and Voucher ID ${productVoucher.voucher?.id}.")
        }

        return productVoucherRepository.save(productVoucher)
    }

    override fun updateProductVoucher(
        id: Long,
        productVoucher: ProductVoucher
    ): ProductVoucher? {
        TODO("Not yet implemented")
    }

    override fun deleteProductVoucher(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getVouchersByProductId(productId: Long): List<Voucher> {
        // The id valid check should be done by product service
        // Product Voucher Service only handle connection between product and voucher
        if (!productService.existsById(productId)) {
            throw IllegalArgumentException("Product with ID $productId does not exist.")
        }

        return productVoucherRepository.findByProductId(productId)
            .mapNotNull { it.voucher } // Exclude nulls just in case
    }


}