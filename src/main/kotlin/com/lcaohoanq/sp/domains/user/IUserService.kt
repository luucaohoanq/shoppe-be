package com.lcaohoanq.sp.domains.user

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.utils.Sortable
import org.springframework.data.domain.Pageable
import java.util.*

interface IUserService {

    fun getAll(): List<UserPort.UserResponse>;
    fun getAll(pageable: Pageable, queryCriteria: QueryCriteria<Sortable.UserSortField>): PageResponse<UserPort.UserResponse>;
    fun getById(id: Long): UserPort.UserResponse?;
    fun isAccountExist(email: String, password: String): User?
    fun findByEmail(email: String): User?
    fun getUserDetailsFromAccessToken(at: String): User
    fun getUserDetailsFromRefreshToken(rf: String): User
    fun doDisableUser(id: Long)

    fun validateAndGetUserExtra(username: String): User
    fun getUserExtra(username: String): Optional<User>
    fun saveUserExtra(user: User): User

}
