package by.project.turamyzba.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "announcement_user")
public class AnnouncementUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "announcement_id")
    private Long announcementId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "created_at")
    private Long createdAt;
    @Column(name = "updated_at")
    private Long updatedAt;

}
