package com.lcaohoanq.sp.domains.discount

import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.domains.product.Product
import jakarta.persistence.*

@Entity
@Table(name = "product_vouchers")
class ProductVoucher(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "voucher_id", nullable = false)
    val voucher: Voucher? = null,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product? = null,

    ) : BaseEntity() {}