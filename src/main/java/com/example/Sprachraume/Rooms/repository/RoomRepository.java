package com.example.Sprachraume.Rooms.repository;

import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.Rooms.entity.Room;
import com.example.Sprachraume.UserData.entity.UserData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    Optional<Room> findById(Long id);

    List<Room> findAllByCreator(UserData userData);

    List<Room> findRoomByParticipantsId(Long participantId);

    List<Room> findByStatusTrue();

    Room findRoomByParticipants(Participant participant);

    List<Room> findRoomsByCreator(UserData userData);

    @Query("SELECT r FROM Room r LEFT JOIN FETCH r.roomOnlineUsers WHERE r.id = :roomId")
    Optional<Room> findRoomWithOnlineUsers(@Param("roomId") Long roomId);
    @Modifying
    @Transactional
    void deleteByCreator(UserData userData);

    Page<Room> findAllByStatus(boolean status , Pageable pageable);
}
