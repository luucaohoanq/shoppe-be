package com.lcaohoanq.sp.mock

import com.lcaohoanq.sp.domains.headquarters.Headquarters
import com.lcaohoanq.sp.domains.headquarters.HeadquartersRepository

fun initHeadquarters(repo: HeadquartersRepository) {
    val headquartersList = listOf(
        Headquarters(region = 185, domainUrl = "https://shopee.sg"),
        Headquarters(region = 93, domainUrl = "https://shopee.co.id"),
        Headquarters(region = 203, domainUrl = "https://shopee.co.th"),
        Headquarters(region = 122, domainUrl = "https://shopee.co.my"),
        Headquarters(region = 222, domainUrl = "https://shopee.vn"),
        Headquarters(region = 164, domainUrl = "https://shopee.ph"),
        Headquarters(region = 27, domainUrl = "https://shopee.br"),
        Headquarters(region = 131, domainUrl = "https://shopee.mx"),
        Headquarters(region = 43, domainUrl = "https://shopee.com.co"),
        Headquarters(region = 40, domainUrl = "https://shopee.cl"),
        Headquarters(region = 200, domainUrl = "https://shopee.tw")
    )

    // Optional: Avoid duplicate insertion if region is unique
    val existingRegions = repo.findAll().map { it.region }.toSet()

    val toInsert = headquartersList.filter { it.region !in existingRegions }

    repo.saveAll(toInsert)
}
