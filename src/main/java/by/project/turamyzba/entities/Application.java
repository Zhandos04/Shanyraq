package by.project.turamyzba.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "applications")
@Data
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private Integer age;

    @ElementCollection
    @CollectionTable(name = "application_phone_numbers", joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "phone_number")
    private List<String> phoneNumbers;

    @Column(name = "applied_date")
    private LocalDateTime appliedDate;

    private String applicationBatchId;
}