package by.project.turamyzba.repositories.anketa;

import by.project.turamyzba.entities.anketa.SurveyInvitationForGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyInvitationForGroupRepository extends JpaRepository<SurveyInvitationForGroup, Long> {
    Optional<SurveyInvitationForGroup> findByToken(String token);
    boolean existsByToken(String token);
}
