package com.lcaohoanq.sp.domains.headquarters

import com.lcaohoanq.sp.exceptions.base.DataNotFoundException
import org.springframework.stereotype.Service

@Service
class HeadquartersService(
    private val headquartersRepository: HeadquartersRepository
) {
    
    fun getAllHeadquarters(): List<Headquarters> {
        return headquartersRepository.findAll()
    }
    
    fun getHeadquartersById(id: Long): Headquarters {
        return headquartersRepository.findById(id)
            .orElseThrow { DataNotFoundException("Headquarters not found with id: $id") }
    }
    
    fun getHeadquartersByRegion(region: Int): Headquarters {
        return headquartersRepository.findByRegion(region)
            .orElseThrow { DataNotFoundException("Headquarters not found for region: $region") }
    }
}
