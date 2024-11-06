package com.example.Sprachraume.Participant.repository;

import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.UserData.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    Optional<Participant> findByRoomAndUser(Room roomId, UserData userID);
}
