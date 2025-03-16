package by.project.turamyzba.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "application_responses")
public class ApplicationResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ResponseStatus status;

    @Column(name = "application_batch_id")
    private String applicationBatchId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}