package com.lcaohoanq.sp.domains.headquarters

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HeadquartersRepository : JpaRepository<Headquarters, Long> {
    fun findByRegion(region: Int): Optional<Headquarters>
}
