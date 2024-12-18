package by.project.turamyzba.entities.anketa;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "question")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Option> options;
}
