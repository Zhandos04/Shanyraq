package by.project.turamyzba.repositories.anketa;

import by.project.turamyzba.entities.anketa.SurveyInvitationForApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyInvitationForApplicationRepository extends JpaRepository<SurveyInvitationForApplication, Long> {
    Optional<SurveyInvitationForApplication> findByToken(String token);
    boolean existsByToken(String token);
}
