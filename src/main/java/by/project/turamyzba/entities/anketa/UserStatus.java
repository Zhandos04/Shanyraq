package by.project.turamyzba.entities.anketa;

import by.project.turamyzba.entities.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_status")
@Data
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_survey_completed")
    private boolean isSurveyCompleted;
}
