package by.project.turamyzba.entities.anketa;

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

    // Идентификатор объявления, для которого создается ссылка
    private Long announcementId;

    // Уникальный токен для формирования ссылки
    private String token;

    private LocalDateTime createdAt;
}
