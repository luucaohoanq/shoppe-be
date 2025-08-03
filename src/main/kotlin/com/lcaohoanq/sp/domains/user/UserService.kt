package com.lcaohoanq.sp.domains.user

import com.lcaohoanq.sp.apis.PageResponse
import com.lcaohoanq.sp.components.JwtTokenUtils
import com.lcaohoanq.sp.entities.UserDeviceToken
import com.lcaohoanq.sp.exceptions.ExpiredTokenException
import com.lcaohoanq.sp.exceptions.UserNotFoundException
import com.lcaohoanq.sp.exceptions.base.DataNotFoundException
import com.lcaohoanq.sp.extension.UserResponseOptions
import com.lcaohoanq.sp.extension.toLoginHistoryResponse
import com.lcaohoanq.sp.extension.toUserResponse
import com.lcaohoanq.sp.extension.toUserSettingsResponse
import com.lcaohoanq.sp.metadata.PaginationMeta
import com.lcaohoanq.sp.metadata.QueryCriteria
import com.lcaohoanq.sp.repositories.LoginHistoryRepository
import com.lcaohoanq.sp.repositories.TokenRepository
import com.lcaohoanq.sp.repositories.UserExtraRepository
import com.lcaohoanq.sp.repositories.UserRepository
import com.lcaohoanq.sp.repositories.UserSettingsRepository
import com.lcaohoanq.sp.utils.SortCriterion
import com.lcaohoanq.sp.utils.SortOrder
import com.lcaohoanq.sp.utils.Sortable
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtTokenUtils: JwtTokenUtils,
    private val tokenRepository: TokenRepository,
    private val loginHistoryRepository: LoginHistoryRepository,
    private val userSettingsRepository: UserSettingsRepository,
    private val userExtraRepository: UserExtraRepository
) : IUserService {

    private val log = KotlinLogging.logger {}

    override fun getAll(): List<UserPort.UserResponse> {
        return userRepository.findAll().map { it.toUserResponse() }
    }

    override fun getAll(
        pageable: Pageable,
        queryCriteria: QueryCriteria<Sortable.UserSortField>
    ): PageResponse<UserPort.UserResponse> {
        val searchSpecification = UserSpecification(queryCriteria.search)

        val sortField = queryCriteria.sortBy.field
        val sortOrder =
            if (queryCriteria.sortOrder == SortOrder.ASC) Sort.Direction.ASC else Sort.Direction.DESC

        val sort = Sort.by(sortOrder, sortField)

        val pageResult = userRepository.findAll(
            searchSpecification,
            PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
        )

        // Fetch login history chỉ khi bạn cần (ví dụ ở đây đang bật mặc định luôn, có thể tùy biến sau)
        val userResponses = pageResult.content.map { user ->
            val top5LoginHistory = loginHistoryRepository
                .findTop5ByUserIdOrderByLoginAtDesc(user.id!!)
                .map { it.toLoginHistoryResponse() }
                .toMutableList()

            // Get user settings for the current user
            val userSettings = userSettingsRepository.findByUserId(userId = user.id!!)
                .toUserSettingsResponse()

            val options = UserResponseOptions(
                includeLoginHistory = true,
                loginHistory = top5LoginHistory,
                includeSettings = true,
                settings = userSettings
            )

            user.toUserResponse(options)
        }

        return PageResponse(
            message = "Get users successfully with query",
            data = userResponses,
            paginationMeta = PaginationMeta(
                totalPages = pageResult.totalPages,
                totalItems = pageResult.totalElements,
                currentPage = pageResult.number,
                pageSize = pageResult.size,
                search = queryCriteria.search,
                sort = SortCriterion(
                    sortBy = queryCriteria.sortBy,
                    order = queryCriteria.sortOrder
                )
            )
        )
    }

    override fun getById(id: Long): UserPort.UserResponse? {
        return userRepository.findById(id).map { user ->
            user.toUserResponse(
                UserResponseOptions(
                    includeLoginHistory = true,
                    loginHistory = loginHistoryRepository
                        .findTop5ByUserIdOrderByLoginAtDesc(user.id!!)
                        .map { it.toLoginHistoryResponse() }
                        .toMutableList(),
                    includeSettings = true,
                    settings = user.userSettings?.toUserSettingsResponse()
                )
            )
        }.orElse(null)
    }

    override fun isAccountExist(email: String, password: String): User? {
        return userRepository.findByEmailAndHashedPassword(email, password)
    }

    override fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email).orElse(null)
            ?: throw DataNotFoundException(
                "Email not found"
            )
    }

    override fun getUserDetailsFromAccessToken(at: String): User {
        if (jwtTokenUtils.isTokenExpired(at)) throw ExpiredTokenException(
            "Token is expired"
        )
        val email = jwtTokenUtils.extractEmail(at)
        return userRepository.findByEmail(email).orElse(null)
            ?: throw DataNotFoundException(
                "User not found"
            )
    }

    override fun getUserDetailsFromRefreshToken(rf: String): User {
        val existingToken = tokenRepository.findByRefreshToken(rf)
            ?: throw DataNotFoundException("Refresh Token not exist")
        return getUserDetailsFromAccessToken(existingToken.token)
    }

    override fun doDisableUser(id: Long) {

        val user = userRepository.findById(id).orElseThrow {
            DataNotFoundException("User not found")
        }
        try{
//            mailFeignClient.sendDisableAccountConfirmationEmail(AuthPort.VerifyEmailReq(
//                email = user.email,
//            ))
        }catch (e: Exception){
            log.error("Error sending disable account confirmation email: ${e.message}")
        }
    }

    override fun validateAndGetUserExtra(username: String): User = getUserExtra(username).orElseThrow {
        UserNotFoundException(
            username
        )
    }

    override fun getUserExtra(username: String): Optional<User> = userRepository.findByUserName(username)

    override fun saveUserExtra(userExtra: UserPort.UserExtraInfo){
        userExtraRepository.save(UserExtra(
            user = userRepository.findById(userExtra.userId)
                .orElseThrow { DataNotFoundException("User with ID ${userExtra.userId} not found") },
            avatar = userExtra.avatar,
            dateOfBirth = userExtra.dateOfBirth ?: ""
        ))
    }
}
