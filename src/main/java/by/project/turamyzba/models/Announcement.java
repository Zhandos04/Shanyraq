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

    private String title;
    private String apartmentsInfo;  
    private String address;
    private String coordsX;
    private String coordsY;
    private String startAt;
    private Integer deposit;
    private Integer maxPeople;
    private String selectedGender;
    private Boolean isCommunalServiceIncluded;
    private String roomiePreferences;
    private Integer MonthlyExpensePerPerson;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> photos;
}

