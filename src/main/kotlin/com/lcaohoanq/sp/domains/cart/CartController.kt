package com.lcaohoanq.sp.domains.cart

import com.lcaohoanq.sp.apis.MyApiResponseV2
import com.lcaohoanq.sp.bases.BaseController
import com.lcaohoanq.sp.domains.auth.IAuthService
import com.lcaohoanq.sp.exceptions.MethodArgumentNotValidException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.prefix}/cart")
@Tag(name = "cart", description = "🛒 Shopping Cart API - Manage user shopping cart")
@SecurityRequirement(name = "JavaInUseSecurityScheme")
class CartController(
    private val cartService: ICartService,
    private val authService: IAuthService
) : BaseController() {

    @Operation(
        summary = "Get user's cart",
        description = "Retrieve the current user's shopping cart with all items"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Cart not found")
        ]
    )
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun getCart(): ResponseEntity<MyApiResponseV2<CartPort.CartResponse?>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        val cart = cartService.getCart(currentUser.id!!)
        return ok("Get cart successfully", cart)
    }

    @Operation(
        summary = "Get or create cart",
        description = "Get existing cart or create new one if doesn't exist"
    )
    @PostMapping("/init")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun getOrCreateCart(): ResponseEntity<MyApiResponseV2<CartPort.CartResponse>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        val cart = cartService.getOrCreateCart(currentUser.id!!)
        return ok("Cart initialized successfully", cart)
    }

    @Operation(
        summary = "Add product to cart",
        description = "Add a product with specified quantity to the user's cart"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product added to cart successfully"),
            ApiResponse(responseCode = "400", description = "Invalid request data or insufficient stock"),
            ApiResponse(responseCode = "404", description = "Product not found")
        ]
    )
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun addToCart(
        @Valid @RequestBody request: CartPort.AddToCartRequest,
        bindingResult: BindingResult
    ): ResponseEntity<MyApiResponseV2<CartPort.CartResponse>> {
        if (bindingResult.hasErrors()) throw MethodArgumentNotValidException(bindingResult)
        
        val currentUser = authService.getCurrentAuthenticatedUser()
        val cart = cartService.addToCart(currentUser.id!!, request)
        return ok("Product added to cart successfully", cart)
    }

    @Operation(
        summary = "Update cart item quantity",
        description = "Update the quantity of a specific product in the cart"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid quantity or insufficient stock"),
            ApiResponse(responseCode = "404", description = "Cart item not found")
        ]
    )
    @PutMapping("/items/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun updateCartItem(
        @Parameter(description = "Product ID", required = true)
        @PathVariable productId: Long,
        @Valid @RequestBody request: CartPort.UpdateCartItemRequest,
        bindingResult: BindingResult
    ): ResponseEntity<MyApiResponseV2<CartPort.CartResponse>> {
        if (bindingResult.hasErrors()) throw MethodArgumentNotValidException(bindingResult)
        
        val currentUser = authService.getCurrentAuthenticatedUser()
        val cart = cartService.updateCartItem(currentUser.id!!, productId, request)
        return ok("Cart item updated successfully", cart)
    }

    @Operation(
        summary = "Remove product from cart",
        description = "Remove a specific product from the user's cart"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product removed from cart successfully"),
            ApiResponse(responseCode = "404", description = "Cart item not found")
        ]
    )
    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun removeFromCart(
        @Parameter(description = "Product ID", required = true)
        @PathVariable productId: Long
    ): ResponseEntity<MyApiResponseV2<CartPort.CartResponse>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        val cart = cartService.removeFromCart(currentUser.id!!, productId)
        return ok("Product removed from cart successfully", cart)
    }

    @Operation(
        summary = "Remove multiple items from cart",
        description = "Remove multiple products from the cart at once"
    )
    @DeleteMapping("/items/batch")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun removeMultipleItems(
        @RequestBody productIds: List<Long>
    ): ResponseEntity<MyApiResponseV2<CartPort.CartResponse>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        val cart = cartService.removeMultipleItems(currentUser.id!!, productIds)
        return ok("Items removed from cart successfully", cart)
    }

    @Operation(
        summary = "Clear cart",
        description = "Remove all items from the user's cart"
    )
    @DeleteMapping("/clear")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun clearCart(): ResponseEntity<MyApiResponseV2<Nothing?>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        cartService.clearCart(currentUser.id!!)
        return ok("Cart cleared successfully", null)
    }

    @Operation(
        summary = "Get cart summary",
        description = "Get summary information about the cart including totals and item availability"
    )
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun getCartSummary(): ResponseEntity<MyApiResponseV2<CartPort.CartSummaryResponse>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        val summary = cartService.getCartSummary(currentUser.id!!)
        return ok("Cart summary retrieved successfully", summary)
    }

    @Operation(
        summary = "Validate cart items",
        description = "Check cart items for availability and stock issues"
    )
    @PostMapping("/validate")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun validateCartItems(): ResponseEntity<MyApiResponseV2<CartPort.CartSummaryResponse>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        val validation = cartService.validateCartItems(currentUser.id!!)
        return ok("Cart validation completed", validation)
    }

    @Operation(
        summary = "Sync cart prices",
        description = "Update cart item prices to match current product prices"
    )
    @PostMapping("/sync-prices")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun syncCartPrices(): ResponseEntity<MyApiResponseV2<CartPort.CartResponse>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        val cart = cartService.syncCartPrices(currentUser.id!!)
        return ok("Cart prices synchronized successfully", cart)
    }

    @Operation(
        summary = "Get cart item count",
        description = "Get the total number of items in the user's cart"
    )
    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun getCartItemCount(): ResponseEntity<MyApiResponseV2<Int>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        val count = cartService.getCartItemCount(currentUser.id!!)
        return ok("Cart item count retrieved successfully", count)
    }

    @Operation(
        summary = "Check if product is in cart",
        description = "Check if a specific product is already in the user's cart"
    )
    @GetMapping("/contains/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')")
    fun isProductInCart(
        @Parameter(description = "Product ID", required = true)
        @PathVariable productId: Long
    ): ResponseEntity<MyApiResponseV2<Boolean>> {
        val currentUser = authService.getCurrentAuthenticatedUser()
        val isInCart = cartService.isProductInCart(currentUser.id!!, productId)
        return ok("Product cart status checked successfully", isInCart)
    }
}
