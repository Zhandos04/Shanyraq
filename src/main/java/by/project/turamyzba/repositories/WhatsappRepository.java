package by.project.turamyzba.repositories;

import by.project.turamyzba.models.Whatsapp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WhatsappRepository extends JpaRepository<Whatsapp, Integer> {
    Optional<Whatsapp> findByPhoneNumber(String phoneNumber);
}
