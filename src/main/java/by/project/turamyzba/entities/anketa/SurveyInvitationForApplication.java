package by.project.turamyzba.entities.anketa;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "survey_invitation_for_application")
@Data
public class SurveyInvitationForApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String applicationBatchId;

    @Column(unique = true)
    private String token;

    private LocalDateTime createdAt;
}