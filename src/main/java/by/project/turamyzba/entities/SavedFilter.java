package by.project.turamyzba.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "saved_filters")
@Builder
public class SavedFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "selected_gender")
    private String selectedGender;

    @Column(name = "region")
    private String region;

    @Column(name = "district")
    private String district;

    @Column(name = "micro_district")
    private String microDistrict;

    @Column(name = "min_price")
    private Integer minPrice;

    @Column(name = "max_price")
    private Integer maxPrice;

    @Column(name = "number_of_people")
    private Integer numberOfPeopleAreYouAccommodating;

    @Column(name = "quantity_of_rooms")
    private String quantityOfRooms;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    // Можно использовать String или LocalDate, если требуется дата.
    @Column(name = "arrive_date")
    private String arriveDate;

    @Column(name = "min_area")
    private Integer minArea;

    @Column(name = "max_area")
    private Integer maxArea;

    @Column(name = "not_the_first_floor")
    private Boolean notTheFirstFloor;

    @Column(name = "not_the_top_floor")
    private Boolean notTheTopFloor;

    @Column(name = "are_pets_allowed")
    private Boolean arePetsAllowed;

    @Column(name = "is_communal_service_included")
    private Boolean isCommunalServiceIncluded;

    @Column(name = "intended_for_students")
    private Boolean intendedForStudents;

    @Column(name = "type_of_housing")
    private String typeOfHousing;

    @Column(name = "for_a_long_time")
    private Boolean forALongTime;

    // Связь с пользователем: у одного пользователя может быть много сохранённых фильтров
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}
