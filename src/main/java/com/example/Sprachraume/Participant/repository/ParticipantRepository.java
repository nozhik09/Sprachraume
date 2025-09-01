package com.example.Sprachraume.Participant.repository;

import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Participant.entity.ParticipantStatus;
import com.example.Sprachraume.Participant.entity.ParticipantType;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.UserData.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant,Long> {
    Optional<Participant> findByRoomAndUser(Room roomId, UserData userID);
    List<Participant> findByUser(UserData userData);

    List<Participant> findAllByRoomAndParticipantTypeAndStatus(Room room, ParticipantType participantType,ParticipantStatus participantStatus);
    List<Participant> findAllByRoomAndStatus(Room room,ParticipantStatus participantStatus);
    List<Participant> findAllByUserAndStatus(UserData user, ParticipantStatus status);

    List<Participant> findAllByUserAndParticipantTypeAndStatus(UserData user, ParticipantType participantType, ParticipantStatus participantStatus);
    List<Participant> findByUserId(Long userId);
    Optional<Participant> findParticipantByUserIdAndRoomId(Long userId, Long roomId);

    List<Participant> findByUserIdAndStatus(Long id, ParticipantStatus participantStatus);
    List<Participant> findAllParticipantByRoom(Room room);

    @Modifying
    @Transactional
    void deleteByUser(UserData user);

}
