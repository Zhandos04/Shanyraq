package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer>, JpaSpecificationExecutor<Announcement> {
    List<Announcement> findAllByUserAndIsArchivedFalseAndIsDeletedFalse(User user);
    List<Announcement> findAllByUserAndIsArchivedTrueAndIsDeletedFalse(User user);
    Optional<Announcement> findById(Long id);
    Page<Announcement> findAllByIsDeletedFalseAndIsArchivedFalse(Pageable pageable);
}
