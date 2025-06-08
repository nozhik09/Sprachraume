package com.example.Sprachraume.Rooms.RoomSchedulerService;

import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.Rooms.repository.RoomRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class RoomSchedulerService {

    private final RoomRepository roomRepository;

    public RoomSchedulerService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Scheduled(fixedRate = 300000) // каждые 5 минут
    public void archiveExpiredRooms() {
        List<Room> activeRooms = roomRepository.findByStatusTrue();
        OffsetDateTime now = OffsetDateTime.now();
        for (Room room : activeRooms) {
            if (room.getEndTime().isBefore(now)) {
                room.setStatus(false);
                roomRepository.save(room);
            }
        }
    }
}
