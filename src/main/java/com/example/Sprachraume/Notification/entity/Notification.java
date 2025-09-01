package com.example.Sprachraume.Notification.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "actor_id")
    private Long actorId;

    @Column(nullable = false, name = "category")
    @Enumerated(EnumType.STRING)
    private NotificationCategory category;      // MESSAGE/INVITE/SYSTEM
    @Column(name = "title")
    private String title;
    @Column(columnDefinition = "text",name = "body")
    private String body;
    @Column(name = "context_type")
    @Enumerated(EnumType.STRING)
    private NotificationContextType contextType;
    @Column(name = "context_id")
    private Long contextId;
    @Column(nullable = false, name = "is_read")
    private boolean isRead = false;
    @Column(nullable = false, name = "created_at")
    private Instant createdAt = Instant.now();


}
