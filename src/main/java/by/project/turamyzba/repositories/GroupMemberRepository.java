package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Group;
import by.project.turamyzba.entities.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findAllByGroup(Group group);
}
