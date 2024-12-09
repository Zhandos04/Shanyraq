package by.project.turamyzba.repositories;

import by.project.turamyzba.dto.responses.AnnouncementResponseForAll;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer>, JpaSpecificationExecutor<Announcement> {
    @Query("SELECT new by.project.turamyzba.dto.responses.AnnouncementResponseForAll(" +
            "a.id, " +
            "MIN(p.url), " +
            "a.title, " +
            "a.address, " +
            "a.arriveDate, " +
            "a.quantityOfRooms, " +
            "a.selectedGender, " +
            "a.numberOfPeopleAreYouAccommodating, " +
            "a.cost, " +
            "a.coordsX, " +
            "a.coordsY, " +
            "a.isArchived) " +
            "FROM Announcement a LEFT JOIN a.photos p " +
            "WHERE a.user = :user " +
            "AND a.isDeleted = false " +
            "AND a.isArchived = false " +
            "GROUP BY a.id, a.title, a.address, a.arriveDate, a.quantityOfRooms, a.selectedGender, a.numberOfPeopleAreYouAccommodating, a.cost " +
            "ORDER BY a.createdAt desc "
    )
    List<AnnouncementResponseForAll> findAllActiveAnnouncementsByUser(@Param("user") User user);

    @Query("SELECT new by.project.turamyzba.dto.responses.AnnouncementResponseForAll(" +
            "a.id, " +
            "MIN(p.url), " +
            "a.title, " +
            "a.address, " +
            "a.arriveDate, " +
            "a.quantityOfRooms, " +
            "a.selectedGender, " +
            "a.numberOfPeopleAreYouAccommodating, " +
            "a.cost, " +
            "a.coordsX, " +
            "a.coordsY, " +
            "a.isArchived) " +
            "FROM Announcement a LEFT JOIN a.photos p " +
            "WHERE a.user = :user " +
            "AND a.isDeleted = false " +
            "AND a.isArchived = true " +
            "GROUP BY a.id, a.title, a.address, a.arriveDate, a.quantityOfRooms, a.selectedGender, a.numberOfPeopleAreYouAccommodating, a.cost, a.coordsX, a.coordsY " +
            "ORDER BY a.createdAt desc "
    )
    List<AnnouncementResponseForAll> findAllArchivedAnnouncementsByUser(@Param("user") User user);

    Optional<Announcement> findById(Long id);

    @Query("SELECT new by.project.turamyzba.dto.responses.AnnouncementResponseForAll(" +
            "a.id, " +
            "MIN(p.url), " +
            "a.title, " +
            "a.address, " +
            "a.arriveDate, " +
            "a.quantityOfRooms, " +
            "a.selectedGender, " +
            "a.numberOfPeopleAreYouAccommodating, " +
            "a.cost, " +
            "a.coordsX, " +
            "a.coordsY, " +
            "a.isArchived) " +
            "FROM Announcement a LEFT JOIN a.photos p " +
            "WHERE a.isDeleted = false AND a.isArchived = false " +
            "GROUP BY a.id, a.title, a.address, a.arriveDate, a.quantityOfRooms, a.selectedGender, a.numberOfPeopleAreYouAccommodating, a.cost ")
    Page<AnnouncementResponseForAll> findAllAnnouncementsDTO(Pageable pageable);

    @Query("SELECT new by.project.turamyzba.dto.responses.AnnouncementResponseForAll(" +
            "a.id, " +
            "MIN(p.url), " +
            "a.title, " +
            "a.address, " +
            "a.arriveDate, " +
            "a.quantityOfRooms, " +
            "a.selectedGender, " +
            "a.numberOfPeopleAreYouAccommodating, " +
            "a.cost, " +
            "a.coordsX, " +
            "a.coordsY, " +
            "a.isArchived) " +
            "FROM Announcement a LEFT JOIN a.photos p " +
            "WHERE a.isDeleted = false AND a.isArchived = false " +
            "AND (:region IS NULL OR :region = 'Весь Казахстан' OR a.region = :region) " +
            "AND (:district IS NULL OR a.district = :district) " +
            "AND (:microDistrict IS NULL OR a.microDistrict = :microDistrict) " +
            "AND (:minPrice IS NULL OR a.cost >= :minPrice) " +
            "AND (:maxPrice IS NULL OR a.cost <= :maxPrice) " +
            "AND (:gender IS NULL OR a.selectedGender = :gender) " +
            "AND (:roommatesCount IS NULL OR a.numberOfPeopleAreYouAccommodating = :roommatesCount) " +
            "GROUP BY a.id, a.title, a.address, a.arriveDate, a.quantityOfRooms, a.selectedGender, a.numberOfPeopleAreYouAccommodating, a.cost ")
    Page<AnnouncementResponseForAll> searchAnnouncementsDTO(
            @Param("region") String region,
            @Param("district") String district,
            @Param("microDistrict") String microDistrict,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("gender") String gender,
            @Param("roommatesCount") Integer roommatesCount,
            Pageable pageable);

    @Query("SELECT new by.project.turamyzba.dto.responses.AnnouncementResponseForAll(" +
            "a.id, " +
            "MIN(p.url), " +
            "a.title, " +
            "a.address, " +
            "a.arriveDate, " +
            "a.quantityOfRooms, " +
            "a.selectedGender, " +
            "a.numberOfPeopleAreYouAccommodating, " +
            "a.cost, " +
            "a.coordsX, " +
            "a.coordsY, " +
            "a.isArchived) " +
            "FROM Announcement a LEFT JOIN a.photos p " +
            "WHERE a.isDeleted = false AND a.isArchived = false " +
            "GROUP BY a.id, a.title, a.address, a.arriveDate, a.quantityOfRooms, a.selectedGender, a.numberOfPeopleAreYouAccommodating, a.cost " +
            "ORDER BY a.createdAt DESC")
    List<AnnouncementResponseForAll> findAllForMap();
}
