package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.Group;
import by.project.turamyzba.entities.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    Optional<Response> findByAnnouncementAndGroup(Announcement announcement, Group group);

    @Query("SELECT r.group FROM Response r WHERE r.announcement = :announcement AND r.status = 'APPROVED'")
    List<Group> findApprovedGroupsByAnnouncement(@Param("announcement") Announcement announcement);
}
