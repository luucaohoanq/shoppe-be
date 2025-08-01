package com.lcaohoanq.sp.domains.token

import com.lcaohoanq.sp.apis.MyApiResponse
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.dto.TokenPort
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.prefix}/tokens")
@Tag(name = "tokens", description = "\uD83D\uDCB5 Token management")
class TokenController(
    private val tokenService: ITokenService
) : BaseController() {

    @GetMapping("/all")
    fun getAllTokens(): ResponseEntity<MyApiResponse<List<TokenPort.TokenResponse>>> {
        return ok(
            message = "Get all tokens successfully",
            data = tokenService.getAll()
        )
    }

}
