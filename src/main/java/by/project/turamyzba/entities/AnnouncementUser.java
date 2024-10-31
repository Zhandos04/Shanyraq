package by.project.turamyzba.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "announcementuser", schema = "TURAMYZBA")
public class AnnouncementUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    private Long announcementId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
