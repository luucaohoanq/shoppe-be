package com.lcaohoanq.sp.domains.localization

import com.lcaohoanq.sp.enums.LocaleSupport
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.MessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/locales")
@Tag(name = "locales", description = "Localization API")
class LocalizeController(
    private val messageSource: MessageSource,
    private val multiLocaleMessageKeyProvider: MultiLocaleMessageKeyProvider
) {

    @GetMapping
    fun getMessage(
        @RequestParam key: String,
        @RequestParam(defaultValue = "en") lang: String
    ): ResponseEntity<String> {
        val locale = Locale.forLanguageTag(lang)
        val message = messageSource.getMessage(key, null, locale)
        return ResponseEntity.ok(message)
    }

    @GetMapping("/{lang}")
    fun getMessageByLang(@PathVariable lang: LocaleSupport): ResponseEntity<Map<String, String>> {
        val locale = Locale.forLanguageTag(lang.toString().lowercase())
        val messages = multiLocaleMessageKeyProvider.getMessagesForLocale(locale)
        return ResponseEntity.ok(messages)
    }

    @GetMapping("/available")
    fun getAllKeysPerLocale(): ResponseEntity<Map<String, List<String>>> {
        return ResponseEntity.ok(multiLocaleMessageKeyProvider.getAllLocaleKeys())
    }
}
