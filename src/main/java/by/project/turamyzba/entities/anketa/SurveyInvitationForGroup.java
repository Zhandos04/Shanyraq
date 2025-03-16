package by.project.turamyzba.entities.anketa;

import by.project.turamyzba.entities.Group;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "survey_invitation_for_group")
@Data
public class SurveyInvitationForGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "group_id", nullable = false, unique = true)
    private Group group;

    @Column(unique = true)
    private String token;

    private LocalDateTime createdAt;
}
