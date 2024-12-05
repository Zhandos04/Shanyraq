package by.project.turamyzba.entities.anketa;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Entity
@Table(name = "option")
@Data
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    public Option(String text, Question question) {
        this.text = text;
        this.question = question;
    }

    public Option() {

    }
}
