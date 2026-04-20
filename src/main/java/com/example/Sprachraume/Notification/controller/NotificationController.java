package com.example.Sprachraume.Notification.controller;


import com.example.Sprachraume.Notification.entity.Notification;
import com.example.Sprachraume.Notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notif")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;


    @GetMapping("/user")
    public Page<Notification> getAllNotifByUserId(@RequestParam Long userId,@RequestParam int page, @RequestParam int size){
        return notificationService.getAllNotifByUserId(userId,page,size);
    }


    @GetMapping("/userstatus")
    public Page<Notification> getAllNotifByUserIdAndStatus(@RequestParam Long userId,@RequestParam int page, @RequestParam int size, @RequestParam boolean isRead){
        return notificationService.getAllNotifByUserIdAndStatus(userId,isRead,page,size);
    }

    @PutMapping("/read")
    public Notification readNotif(@RequestParam Long notifId,@RequestParam Long currentUserId){
        return notificationService.getNotifById(notifId,currentUserId);
    }




//    @GetMapping("actor")
//    public List<Notification> getAllNotifByActorId(@RequestParam Long actorId){
//        return notificationService.getAllNotifByActorId(actorId);
//    }
}
