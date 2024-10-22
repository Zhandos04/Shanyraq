package by.project.turamyzba.models;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Announcements")
@Builder
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "apartments_info")
    private String apartmentsInfo;  
    @Column(name = "address")
    private String address;
    @Column(name = "coords")
    private String coords
    //private String coordsX;
    // private String coordsY;
    private String startAt;
    @Column(name = "deposit")
    private Integer deposit;
    @Column(name = "max_people")
    private Integer maxPeople;
    @Column(name = "selected_gender")
    private String selectedGender;
    @Column(name = "is_communal_service_include")
    private Boolean isCommunalServiceIncluded;
    @Column(name = "roomie_preferences")
    private String roomiePreferences;
    @Column(name = "monthly_expense_per_person")
    private Integer MonthlyExpensePerPerson;
    @Column(name = "status")
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> photos;
  
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
