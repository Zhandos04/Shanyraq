package by.project.turamyzba.repositories;

import by.project.turamyzba.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByParentidOrderByNamekazAsc(Long parentId);
}
