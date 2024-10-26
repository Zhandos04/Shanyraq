package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Integer> {
    Optional<Phone> findByPhoneNumber(String phoneNumber);
}
