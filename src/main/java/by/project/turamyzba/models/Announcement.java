package by.project.turamyzba.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "announcements")
@Data
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "apartments_info")
    private String apartmentsInfo;

    @Column(name = "address")
    private String address;

    @Column(name = "coords")
    private String coords;

    @Column(name = "deposit")
    private BigDecimal deposit;

    @Column(name = "max_people")
    private int maxPeople;

    @Column(name = "selected_gender")
    private String selectedGender;

    @Column(name = "is_communal_service_include")
    private Boolean isCommunalServiceInclude;

    @Column(name = "roomie_preferences")
    private String roomiePreferences;

    @Column(name = "monthly_expense_per_person")
    private BigDecimal monthlyExpensePerPerson;

    @Column(name = "status")
    private String status;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
