package com.lcaohoanq.sp.domains.notifications;


import com.lcaohoanq.sp.domains.notifications.FcmTokenPort.CreateUserDeviceTokenReq;
import com.lcaohoanq.sp.domains.notifications.FcmTokenPort.UpdateUserDeviceTokenReq;
import com.lcaohoanq.sp.domains.notifications.FcmTokenPort.UserDeviceTokenDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FcmTokenService {
    Page<UserDeviceTokenDto> getAll(Pageable pageable);
    UserDeviceTokenDto getById(Long id);
    UserDeviceTokenDto create(CreateUserDeviceTokenReq req);
    UserDeviceTokenDto update(Long id, UpdateUserDeviceTokenReq req);
    UserDeviceTokenDto createOrUpdate(CreateUserDeviceTokenReq req);
    List<String> getFcmTokensByEmail(String email);
}
