package by.project.turamyzba.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "responses")
@Data
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "advertisement_id")
    private Announcement announcement;

    @Enumerated(EnumType.STRING)
    private ResponseStatus status;
}