package com.example.Sprachraume.Rooms.entity;


import com.example.Sprachraume.Category.entity.Category;
import com.example.Sprachraume.Participant.entity.Participant;
import com.example.Sprachraume.UserData.entity.UserData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room")
@Entity
@EqualsAndHashCode(exclude = {"creator", "participants"})
public class Room {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "topic")
    private String topic;
    @Column(name = "start_time")
    private OffsetDateTime startTime;
    @Column(name = "end_Time")
    private OffsetDateTime  endTime;

    @Column(name = "duration")
    private Long duration;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "private_room")
    private Boolean privateRoom;

    @Column(name = "language_lvl")
    private String languageLvl;

    @Column(name = "quantity_participant")
    private Long quantityParticipant;

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


    @ManyToMany
    @JoinTable(
            name = "room_online_users",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserData> roomOnlineUsers=new HashSet<>();

    @Column(name = "count_online_users")
    private Long countOnlineUser;


    @Column(name = "room_url")
    private String roomUrl;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Participant> participants;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private UserData creator;

}
