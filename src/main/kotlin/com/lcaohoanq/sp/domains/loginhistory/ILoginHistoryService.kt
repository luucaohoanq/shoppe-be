package com.lcaohoanq.sp.domains.loginhistory

import com.lcaohoanq.sp.domains.user.User

interface ILoginHistoryService {

    fun recordLogin(user: User, ipAddress: String, userAgent: String)

}
