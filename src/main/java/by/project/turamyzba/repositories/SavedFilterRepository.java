package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.SavedFilter;
import by.project.turamyzba.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedFilterRepository extends JpaRepository<SavedFilter, Long> {
    List<SavedFilter> findByUser(User user);
}

