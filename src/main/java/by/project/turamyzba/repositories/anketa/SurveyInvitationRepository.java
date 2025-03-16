package by.project.turamyzba.repositories.anketa;

import by.project.turamyzba.entities.anketa.SurveyInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyInvitationRepository extends JpaRepository<SurveyInvitation, Long> {
    Optional<SurveyInvitation> findByToken(String token);
    boolean existsByToken(String token);
}