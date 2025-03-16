package by.project.turamyzba.entities.anketa;

import by.project.turamyzba.entities.Announcement;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "survey_invitation")
@Data
public class SurveyInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "announcement_id", nullable = false, unique = true)
    private Announcement announcement;

    @Column(unique = true)
    private String token;

    private LocalDateTime createdAt;
}
