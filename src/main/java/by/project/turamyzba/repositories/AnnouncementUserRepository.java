package by.project.turamyzba.repositories;

import by.project.turamyzba.models.AnnouncementUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementUserRepository extends JpaRepository<AnnouncementUser, Long> {
    List<AnnouncementUser> findByUserId(Long userId);
}
