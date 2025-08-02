package com.lcaohoanq.sp.domains.discount

import com.lcaohoanq.sp.bases.BaseEntity
import com.lcaohoanq.sp.enums.DiscountType
import jakarta.persistence.*
import net.minidev.json.annotate.JsonIgnore
import java.time.LocalDateTime

@Entity
@Table(name = "vouchers")
class Voucher(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    val id: Long? = null,

    val code: String,

    @Enumerated(EnumType.STRING)
    val discountType: DiscountType, // PERCENTAGE or AMOUNT
    val discountValue: Double, // 10.0 = 10% or $10

    val validFrom: LocalDateTime,
    val validUntil: LocalDateTime,

    var expired: Boolean = false,

    @OneToMany(mappedBy = "voucher", cascade = [CascadeType.ALL])
    @JsonIgnore
    var productVouchers: MutableSet<ProductVoucher> = mutableSetOf()

    ) : BaseEntity() {

}