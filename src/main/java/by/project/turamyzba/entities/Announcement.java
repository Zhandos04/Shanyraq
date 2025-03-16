package by.project.turamyzba.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Column(name = "role")
    private String role;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "selected_gender")
    private String selectedGender;
    @Column(name = "do_you_in_this_house")
    private Boolean doYouLiveInThisHouse;
    @Column(name = "how_many_people_live_in_this_apartment")
    private String howManyPeopleLiveInThisApartment;
    @Column(name = "number_of_people_are_you_accommodating")
    private Integer numberOfPeopleAreYouAccommodating;
    @Column(name = "min_age")
    private Integer minAge;
    @Column(name = "max_age")
    private Integer maxAge;
    @Column(name = "region")
    private String region;
    @Column(name = "district")
    private String district;
    @Column(name = "micro_district")
    private String microDistrict;
    @Column(name = "address")
    private String address;
    @Column(name = "arrive_data")
    private LocalDate arriveDate;
    @Column(name = "cost")
    private Integer cost;
    @Column(name = "quantity_of_rooms")
    private String quantityOfRooms;
    @Column(name = "is_deposit_required")
    private Boolean isDepositRequired;
    @Column(name = "deposit")
    private Integer deposit;
    @Column(name = "are_pets_allowed")
    private Boolean arePetsAllowed;
    @Column(name = "is_communal_service_include")
    private Boolean isCommunalServiceIncluded;
    @Column(name = "min_amount_of_communal_service")
    private Integer minAmountOfCommunalService;
    @Column(name = "max_amount_of_communal_service")
    private Integer maxAmountOfCommunalService;
    @Column(name = "intended_for_students")
    private Boolean intendedForStudents;
    @Column(name = "are_bad_habits_allowed")
    private Boolean areBadHabitsAllowed;
    @Column(name = "apartments_info")
    private String apartmentsInfo;
    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> photos;
    @Column(name = "type_of_housing")
    private String typeOfHousing;
    @Column(name = "number_of_floor")
    private Integer numberOfFloor;
    @Column(name = "max_floor_in_the_building")
    private Integer maxFloorInTheBuilding;
    @Column(name = "area_of_the_apartment")
    private Integer areaOfTheApartment;
    @Column(name = "for_a_long_time")
    private Boolean forALongTime;
    @Column(name = "owners_name")
    private String ownersName;
    @ElementCollection
    @CollectionTable(name = "advertisement_phone_numbers", joinColumns = @JoinColumn(name = "advertisement_id"))
    @Column(name = "phone_number")
    private List<String> ownersPhoneNumbers;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "announcement_id")
    private List<ResidentData> residentData;
    @ElementCollection
    @CollectionTable(name = "advertisement_preferences", joinColumns = @JoinColumn(name = "advertisement_id"))
    @Column(name = "preference")
    private List<String> preferences;
    @Column(name = "coordsX")
    private String coordsX;
    @Column(name = "coordsY")
    private String coordsY;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
    @Column(name = "is_archived")
    private Boolean isArchived;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Column(name = "considering_only_n_people")
    private Boolean consideringOnlyNPeople;

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
