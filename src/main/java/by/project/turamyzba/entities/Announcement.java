package by.project.turamyzba.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "announcements")
@Builder
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "selected_gender")
    private String selectedGender;
    @Column(name = "apartments_info")
    private String apartmentsInfo;
    @Column(name = "max_people")
    private Integer maxPeople;
    @Column(name = "number_of_rooms_in_the_apartment")
    private Integer numberOfRoomsInTheApartment;
    @Column(name = "min_age")
    private Integer minAge;
    @Column(name = "max_age")
    private Integer maxAge;
    @Column(name = "address")
    private String address;
    @Column(name = "coordsX")
    private String coordsX;
    @Column(name = "coordsY")
    private String coordsY;
    @Column(name = "arrive_data")
    private LocalDate arriveDate;
    @Column(name = "cost")
    private Integer cost;
    @Column(name = "is_deposit_required")
    private Boolean isDepositRequired;
    @Column(name = "deposit")
    private Integer deposit;
    @Column(name = "is_communal_service_include")
    private Boolean isCommunalServiceIncluded;
    @Column(name = "are_pets_allowed")
    private Boolean arePetsAllowed;
    @Column(name = "intended_for_students")
    private Boolean intendedForStudents;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "is_archived")
    private Boolean isArchived;
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> photos;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnnouncementUser> announcementUsers;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
