package com.lcaohoanq.sp.repositories;

import com.lcaohoanq.sp.entities.NotificationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserEmail(String email);
    List<NotificationEntity> findByUserEmailOrderByCreatedAtDesc(String email);

}
