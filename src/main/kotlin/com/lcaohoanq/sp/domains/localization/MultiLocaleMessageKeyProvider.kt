package com.lcaohoanq.sp.domains.localization

import org.springframework.stereotype.Component
import java.util.*

@Component
class MultiLocaleMessageKeyProvider {

    private val supportedLocales = listOf("en", "vi", "jp")

    private val localeToProperties: Map<String, Properties> = loadAllLocaleProperties()

    private fun loadAllLocaleProperties(): Map<String, Properties> {
        val classLoader = Thread.currentThread().contextClassLoader
        return supportedLocales.associateWith { lang ->
            val filePath = "i18n/messages_$lang.properties"
            val props = Properties()
            classLoader.getResource(filePath)?.openStream()?.use {
                props.load(it)
            }
            props
        }
    }

    fun getMessagesForLocale(locale: Locale): Map<String, String> {
        val lang = locale.language
        val props = localeToProperties[lang] ?: return emptyMap()
        return props.stringPropertyNames()
            .associateWith { key -> props.getProperty(key) }
            .toSortedMap()
    }

    fun getAllLocaleKeys(): Map<String, List<String>> {
        return localeToProperties.mapValues { (_, props) ->
            props.stringPropertyNames().sorted()
        }
    }
}
