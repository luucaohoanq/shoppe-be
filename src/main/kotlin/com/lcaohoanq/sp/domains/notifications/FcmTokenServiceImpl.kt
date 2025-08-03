package com.lcaohoanq.sp.domains.notifications

import com.lcaohoanq.sp.domains.notifications.FcmTokenPort.CreateUserDeviceTokenReq
import com.lcaohoanq.sp.domains.notifications.FcmTokenPort.UpdateUserDeviceTokenReq
import com.lcaohoanq.sp.entities.UserDeviceToken
import com.lcaohoanq.sp.exceptions.base.DataNotFoundException
import com.lcaohoanq.sp.repositories.UserDeviceTokenRepository
import com.lcaohoanq.sp.repositories.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
@RequiredArgsConstructor
class FcmTokenServiceImpl(
    private val repository: UserDeviceTokenRepository,
    private val userRepository: UserRepository,
) : FcmTokenService {


    override fun getAll(pageable: Pageable): Page<UserDeviceToken> =
        repository.findAll(pageable)


    override fun getById(id: Long): UserDeviceToken {
        return repository.findById(id)
            .orElseThrow { DataNotFoundException("User device token not found") }
    }

    override fun create(req: CreateUserDeviceTokenReq): UserDeviceToken {
        val user = userRepository.findById(req.userId).orElseThrow {
            DataNotFoundException("User not found")
        }

        val entity = UserDeviceToken(
            user = user,
            deviceId = req.deviceId,
            fcmToken = req.fcmToken,
            deviceName = req.deviceName,
            platform = req.platform
        )

        return toDto(repository.save(entity))
    }

    override fun update(id: Long, req: UpdateUserDeviceTokenReq): UserDeviceToken {
        val entity = getById(id)

        if (req.fcmToken != null) entity.fcmToken = req.fcmToken
        if (req.deviceName != null) entity.deviceName = req.deviceName
        if (req.platform != null) entity.platform = req.platform

        return toDto(repository.save(entity))
    }

    override fun createOrUpdate(req: CreateUserDeviceTokenReq): UserDeviceToken {
        val user = userRepository.findById(req.userId).orElseThrow {
            DataNotFoundException("User not found")
        }

        val updatedReq = CreateUserDeviceTokenReq(
            user.id!!,
            req.deviceId,
            req.fcmToken,
            req.deviceName,
            req.platform
        )

        // Try to find existing token for this user and device
        val existingToken =
            repository.findByUserIdAndDeviceId(updatedReq.userId, updatedReq.deviceId)

        if (existingToken.isPresent) {
            // Update existing token
            val entity = existingToken.get()
            entity.fcmToken = updatedReq.fcmToken
            entity.deviceName = updatedReq.deviceName
            entity.platform = updatedReq.platform
            return toDto(repository.save(entity))
        } else {
            // Create new token
            return create(updatedReq)
        }
    }

    override fun getFcmTokensByEmail(email: String): MutableList<String> {
        return repository.findAllByUserEmail(email)
            .stream()
            .map(UserDeviceToken::fcmToken)
            .collect(Collectors.toList())
    }

    private fun toDto(entity: UserDeviceToken): UserDeviceToken {
        return UserDeviceToken(
            entity.id,
            entity.user,
            entity.deviceId,
            entity.fcmToken,
            entity.deviceName,
            entity.platform
        )
    }
}
