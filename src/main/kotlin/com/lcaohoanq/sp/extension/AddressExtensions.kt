package com.lcaohoanq.sp.extension

import com.lcaohoanq.sp.domains.user.Address
import com.lcaohoanq.sp.dto.AddressPort

fun Address.toAddressResponse(): AddressPort.AddressResponse {
    return AddressPort.AddressResponse(
        id = this.id!!,
        nameOfUser = this.nameOfUser,
        phoneNumber = this.phoneNumber,
        address = this.address,
        isDefault = this.isDefault
    )
}

fun Set<Address>?.toAddressResponseSet(): Set<AddressPort.AddressResponse>? {
    return this?.map { it.toAddressResponse() }?.toSet()
}
