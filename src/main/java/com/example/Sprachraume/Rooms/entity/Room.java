package com.example.Sprachraume.Rooms.entity;


import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.UserData.entity.UserData;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Room {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "topic")
    private String topic;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "age")
    private Long age;
    @Column(name = "language")
    private String language;
    @Column(name = "min_quantity")
    private Long minQuantity;
    @Column(name = "max_quantity")
    private Long maxQuantity;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "participant_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id"))
    private Set<Participant> participants;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    @JsonBackReference("creator-rooms")
    private UserData creator;  // Связь с создателем комнаты

}
