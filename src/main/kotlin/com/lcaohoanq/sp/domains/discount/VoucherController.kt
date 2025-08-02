package com.lcaohoanq.sp.domains.discount

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.utils.createPageRequest
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.prefix}/vouchers")
@Tag(name = "vouchers", description = "Voucher API integration")
class VoucherController(
    private val service: VoucherService
) : BaseController() {

    @GetMapping("/paged")
    fun getPageable(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "id,asc") sort: String
    ): ResponseEntity<MyApiResponse<Page<Voucher>>> {
        return ok(data = service.getAll(createPageRequest(page, size, sort)))
    }

    @GetMapping("/{id}")
    fun getVoucherById(@PathVariable id: Long): ResponseEntity<MyApiResponse<Voucher>> =
        ok(data = service.getVoucherById(id))

    @PostMapping("")
    fun createVoucher(@Valid @RequestBody voucher: Voucher): ResponseEntity<MyApiResponse<Voucher>> =
        ok(data = service.createVoucher(voucher))

    @DeleteMapping("/{id}")
    fun deleteVoucher(@PathVariable id: Long): ResponseEntity<MyApiResponse<Any>>
        = ok(data = service.deleteVoucher(id))

    @GetMapping("/product-vouchers")
    fun getAllProductVouchers(): ResponseEntity<MyApiResponse<List<ProductVoucher>>> =
        ok(data = service.getAllProductVouchers())

    @GetMapping("/product-vouchers/{id}")
    fun getProductVoucherById(@PathVariable id: Long): ResponseEntity<MyApiResponse<ProductVoucher>> =
        ok(data = service.getProductVoucherById(id))

    @PostMapping("/product-vouchers")
    fun createProductVoucher(@Valid @RequestBody productVoucher: ProductVoucher): ResponseEntity<MyApiResponse<ProductVoucher>> =
        ok(data = service.createProductVoucher(productVoucher))


}