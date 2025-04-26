package com.example.Sprachraume.Rooms.repository;

import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.UserData.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findById(Long id);

    List<Room> findAllByCreator (UserData userData);
    List<Room> findRoomByParticipantsId(Long participantId);
    List<Room> findByStatusTrue();
}
