package by.project.turamyzba.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.util.List;

@Embeddable
@Data
public class ResidentPhones {

    @ElementCollection
    @CollectionTable(name = "resident_phone_numbers", joinColumns = @JoinColumn(name = "resident_name"))
    @Column(name = "phone_number")
    private List<String> phoneNumbers;
}

