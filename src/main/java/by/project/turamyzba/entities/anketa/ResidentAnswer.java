package by.project.turamyzba.entities.anketa;

import by.project.turamyzba.entities.Resident;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "resident_answer")
@Data
public class ResidentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;
}
