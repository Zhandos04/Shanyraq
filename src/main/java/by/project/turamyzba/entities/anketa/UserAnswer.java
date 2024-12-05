package by.project.turamyzba.entities.anketa;

import by.project.turamyzba.entities.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Entity
@Table(name = "user_answer")
@Data
@RedisHash
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;
}
