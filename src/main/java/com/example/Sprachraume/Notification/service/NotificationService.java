package com.example.Sprachraume.Notification.service;

import com.example.Sprachraume.Exceptions.Exception.ForbiddenException;
import com.example.Sprachraume.Exceptions.Exception.NotificationNotFoundException;
import com.example.Sprachraume.Exceptions.Exception.UserNotFoundException;
import com.example.Sprachraume.Notification.entity.Notification;
import com.example.Sprachraume.Notification.entity.NotificationCategory;
import com.example.Sprachraume.Notification.repository.NotificationRepository;
import com.example.Sprachraume.UserData.entity.UserData;
import com.example.Sprachraume.UserData.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    public Page<Notification> getAllNotifByUserId(Long userId,int page,int size){
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return notificationRepository.findAllByUserId(userId, PageRequest.of(page,size));
    }



    public Page<Notification> getAllNotifByUserIdAndStatus(Long userId,boolean isRead,int page,int size){
        UserData user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return   notificationRepository.findAllByUserIdAndIsRead(userId,isRead,PageRequest.of(page,size));
    }

    @Transactional
    public Notification getNotifById(Long notifId, Long currentUserId) {
        Notification n = notificationRepository.findById(notifId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));
        if (!n.getUserId().equals(currentUserId)) throw new ForbiddenException("Not your notification");
        if (!n.isRead()) n.setRead(true); // помечаем прочитанным
        return n; // Hibernate сам зафлашит в конце транзакции
    }



    @Transactional
    public void markRead(Long userId, Long notifId) {
        var n = notificationRepository.findById(notifId).orElseThrow();
        if (!n.getUserId().equals(userId)) throw new RuntimeException("Forbidden");
        n.setRead(true);
        notificationRepository.save(n);
    }

    @Transactional
    public int markAllRead(Long userId) {
        return notificationRepository.markAllRead(userId);
    }




    //    public void addMessageNotif(Long recipientId, Long senderId, Long messageId, String preview) {
//        var n = new Notification();
//        n.setUserId(recipientId);
//        n.setActorId(senderId);
//        n.setCategory(NotificationCategory.MESSAGE);
//        n.setTitle("Новое сообщение");
//        n.setBody(preview);
//        n.setContextType("MESSAGE");
//        n.setContextId(messageId);
//        notificationRepository.save(n);
//    }
    public void addSystemNotif(Long userId, String title, String body) {
        var n = new Notification();
        n.setUserId(userId);
        n.setCategory(NotificationCategory.SYSTEM);
        n.setTitle(title);
        n.setBody(body);
        notificationRepository.save(n);
    }

//    public List<Notification> getAllNotifByActorId(Long actorId){
//        UserData user = userRepository.findById(actorId)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        return   notificationRepository.findAllByActorId(actorId);
//    }

}
