package com.example.Sprachraume.Notification.repository;


import com.example.Sprachraume.Notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable p);
    Page<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId, Pageable p);
    @Modifying
    @Query("update Notification n set n.isRead = true where n.userId = :userId and n.isRead = false")
    int markAllRead(@Param("userId") Long userId);
    Page<Notification> findAllByUserId(Long userId,Pageable pageable);
    List<Notification> findAllByActorId(Long actorId);

    Page<Notification> findAllByUserIdAndIsRead(Long userId, boolean isRead,Pageable pageable);
    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
