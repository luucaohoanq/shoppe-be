package com.lcaohoanq.sp.domains.loginhistory

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.prefix}/ips")
@Tag(name = "ips", description = "\uD83C\uDF0E IP API, for getting geo location from IP address")
class IpController(
    private val geoLocationService: GeoLocationService
) {
    @GetMapping("/location")
    fun getLocation(@RequestParam ip: String): ResponseEntity<GeoLocation> {
        val location = geoLocationService.getLocationFromIp(ip)
        return ResponseEntity.ok(location)
    }
}
