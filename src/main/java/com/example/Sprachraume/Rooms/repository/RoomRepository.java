package com.example.Sprachraume.Rooms.repository;

import com.example.Sprachraume.Rooms.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    Optional<Room> findById (Long id);

}
