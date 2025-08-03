package com.lcaohoanq.sp.utils

import com.lcaohoanq.sp.exceptions.AccessDeniedException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

object AuthenticationUtils {

    fun extractUserId(): String {
        val authentication = getAuthentication()

        if (authentication is AnonymousAuthenticationToken) {
            throw AccessDeniedException()
        }

        val jwt = (authentication as JwtAuthenticationToken).token
        
        // Try to extract user ID from different possible claim names
        return jwt.getClaim<String>("userId")
            ?: jwt.getClaim<String>("user_id") 
            ?: jwt.getClaim<String>("sub")
            ?: throw AccessDeniedException()
    }

    fun extractJwt(): String =
        (getAuthentication().principal as Jwt).tokenValue

    fun getAuthentication(): Authentication =
        SecurityContextHolder.getContext().authentication

    fun isAuthenticated(): Boolean =
        getAuthentication().let { it !is AnonymousAuthenticationToken && it.isAuthenticated }

    fun getUserId(): Long? {
        return try {
            val authentication = getAuthentication()
            if (authentication is AnonymousAuthenticationToken || !authentication.isAuthenticated) {
                return null
            }
            
            val jwt = (authentication as JwtAuthenticationToken).token
            
            // Try to extract user ID from different possible claim names and convert to Long
            val userIdStr = jwt.getClaim<String>("userId")
                ?: jwt.getClaim<String>("user_id")
                ?: jwt.getClaim<String>("sub")
                ?: return null
                
            userIdStr.toLongOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    fun getUserEmail(): String? {
        return try {
            val authentication = getAuthentication()
            if (authentication is AnonymousAuthenticationToken || !authentication.isAuthenticated) {
                return null
            }
            
            val jwt = (authentication as JwtAuthenticationToken).token
            jwt.getClaim<String>("email") ?: jwt.getClaim<String>("preferred_username")
        } catch (e: Exception) {
            null
        }
    }
}

