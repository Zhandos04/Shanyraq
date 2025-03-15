package by.project.turamyzba.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class ResidentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "resident_phone_numbers", joinColumns = @JoinColumn(name = "resident_id"))
    @Column(name = "phone_number")
    private List<String> phoneNumbers;
}
