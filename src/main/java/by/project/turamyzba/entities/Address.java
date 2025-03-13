package by.project.turamyzba.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {
    @Id
    private Long id;

    private Long parentid;

    private Boolean haschild;

    private String atetypenamerus;
    private String atetypenamekaz;

    private String namerus;
    private String namekaz;
}