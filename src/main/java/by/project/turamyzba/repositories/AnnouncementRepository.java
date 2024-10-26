package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    List<Announcement> findByUserId(Long userId);
    Optional<Announcement> findById(Long id);
}
