package by.project.turamyzba.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "anketa")
@Data
public class Anketa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "number_of_question")
    private Integer numberOfQuestion;
    @Lob
    @Column(name = "answer_text")
    private String answerText;
}
