package by.project.turamyzba.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "group_members")
@Data
public class GroupMember {

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
    @CollectionTable(name = "group_member_phone_numbers", joinColumns = @JoinColumn(name = "group_member_id"))
    @Column(name = "phone_number")
    private List<String> phoneNumbers;

    @Enumerated(EnumType.STRING)
    private GroupMemberStatus status;

    @Column(name = "applied_date")
    private LocalDateTime appliedDate;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;
}