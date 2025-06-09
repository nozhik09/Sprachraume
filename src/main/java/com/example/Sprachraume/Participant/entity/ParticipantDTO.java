package com.example.Sprachraume.Participant.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDTO {
    private Long userId;
    private String username;
    private ParticipantStatus status;
    private ParticipantType participantType;
}
