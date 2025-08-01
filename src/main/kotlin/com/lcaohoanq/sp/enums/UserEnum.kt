package com.lcaohoanq.sp.enums

class UserEnum {

    enum class Status {
        UNVERIFIED, VERIFIED, BLOCKED, DEACTIVATED
    }

    enum class Role {
        CUSTOMER, SHOP, STAFF, ADMIN, MANAGER
    }

    enum class Gender {
        MALE, FEMALE, OTHERS
    }

}
