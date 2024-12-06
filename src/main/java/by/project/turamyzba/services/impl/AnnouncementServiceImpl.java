package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.AnnouncementFilterRequest;
import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.dto.responses.AnnouncementResponseForAll;
import by.project.turamyzba.mappers.AnnouncementMapper;
import by.project.turamyzba.entities.Announcement;
import by.project.turamyzba.entities.Image;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.repositories.AnnouncementRepository;
import by.project.turamyzba.repositories.UserRepository;
import by.project.turamyzba.services.AnnouncementService;
import by.project.turamyzba.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    @Value("${2gis.api.key}")
    private String apiKey;

    @Value("${2gis.api.url}")
    private String apiUrl;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final AnnouncementRepository announcementRepository;

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;
    @Transactional
    @Override
    public void createAnnouncement(AnnouncementRequest announcementRequest) throws IOException {
        String[] coords = getCoordsFromAddress(announcementRequest.getRegion() + ", " + announcementRequest.getDistrict() + ", " + announcementRequest.getMicroDistrict() + ", " + announcementRequest.getAddress());

        if (coords.length < 2) {
            throw new BadRequestException("Unable to determine coordinates for the given address");
        }
        User user =  userRepository.findByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("User: {}", user);

        Announcement announcement = AnnouncementMapper.toEntity(announcementRequest, coords);

        List<Image> images = AnnouncementMapper.toImages(announcementRequest.getImages(), announcement);
        announcement.setPhotos(images);
        announcement.setUser(user);

        announcementRepository.save(announcement);
    }

    private String[] getCoordsFromAddress(String address) {
        String[] coords = new String[2];
        String response = restTemplate.getForObject(apiUrl + "/geocode?q=" + address + "&fields=items.point&key=" + apiKey, String.class);
        log.info("Response from 2GIS API: {}", response);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            coords[0] = objectMapper.readTree(response).get("result").get("items").get(0).get("point").get("lat").asText();
            coords[1] = objectMapper.readTree(response).get("result").get("items").get(0).get("point").get("lon").asText();
        } catch (Exception e) {
            log.error("Error while parsing 2GIS API response: {}", e.getMessage());
        }

        return coords;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Announcement> getAllRoommateListings(Pageable pageable) {
        log.info("Getting all roommate listings");
        return announcementRepository.findAllByIsDeletedFalse(pageable);
    }

    @Override
    @Transactional
    public Page<Announcement> searchRoommateListings(String region, Integer minPrice, Integer maxPrice, String gender, Integer roommatesCount, Pageable pageable) {
        log.info("Searching roommate listings with filters: city={}, minPrice={}, maxPrice={}, gender={}, roommatesCount={}",
                region, minPrice, maxPrice, gender, roommatesCount);
        Specification<Announcement> spec = Specification.where(null);

        if (region != null && !region.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("region"), region));
        }
        if (minPrice != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("cost"), maxPrice));
        }
        if (gender != null && !gender.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("selectedGender"), gender));
        }
        if (roommatesCount != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("numberOfPeopleAreYouAccommodating"), roommatesCount));
        }

        return announcementRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponse getAnnouncementById(Long id) {
        log.info("Getting announcement by id: {}", id);
        return toAnnouncementResponse(announcementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Announcement not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getUserAnnouncements(){
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return announcementRepository.findAllByUserAndIsArchivedFalseAndIsDeletedFalse(user).stream()
                .map(this::toAnnouncementResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getUserArchiveAnnouncements() {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return announcementRepository.findAllByUserAndIsArchivedTrueAndIsDeletedFalse(user).stream()
                .map(this::toAnnouncementResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void archiveAnnouncement(Long announcementId) throws BadRequestException {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("Обьявление не найдено"));
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if(announcement.getUser().equals(user)) {
            announcement.setIsArchived(true);
        } else {
            throw new BadRequestException("bad request!");
        }
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional
    public void restoreAnnouncement(Long announcementId) throws BadRequestException {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("Обьявление не найдено"));
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if(!announcement.getIsDeleted() && announcement.getUser().equals(user)){
            announcement.setIsArchived(false);
        } else {
            throw new BadRequestException("bad request!");
        }

        announcementRepository.save(announcement);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long announcementId) throws BadRequestException {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("Обьявление не найдено"));
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if(announcement.getIsArchived() && announcement.getUser().equals(user)) {
            announcement.setIsDeleted(true);
        } else {
            throw new BadRequestException("bad request!");
        }
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> getFilteredAnnouncements(AnnouncementFilterRequest request) {
        return announcementRepository.findAll((Specification<Announcement>) (root, query, criteriaBuilder) -> {
            // 1. Построение приоритетной сортировки
            List<Order> orders = new ArrayList<>();

            // Приоритет 1: Цена (Price) (minPrice и maxPrice)
            if (request.getMinPrice() != null && request.getMaxPrice() != null) {
                // Выражение: 1 если cost между minPrice и maxPrice, иначе 0
                Expression<Object> pricePriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.between(root.get("cost"), request.getMinPrice(), request.getMaxPrice()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(pricePriority));
            }

            // Приоритет 2: Гендер (Gender)
            if (request.getSelectedGender() != null && !request.getSelectedGender().isEmpty()) {
                Expression<Object> genderPriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(root.get("selectedGender"), request.getSelectedGender()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(genderPriority));
            }

            // Приоритет 3: Регион (Region)
            if (request.getRegion() != null && !request.getRegion().isEmpty()) {
                Expression<Object> regionPriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(root.get("region"), request.getRegion()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(regionPriority));
            }

            // Приоритет 4: Дата (Date) (arriveDate)
            if (request.getArriveDate() != null) {
                Expression<Object> datePriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(root.get("arriveDate"), request.getArriveDate()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(datePriority));
            }

            // Приоритет 5: Район (District)
            if (request.getDistrict() != null && !request.getDistrict().isEmpty()) {
                Expression<Object> districtPriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(root.get("district"), request.getDistrict()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(districtPriority));
            }

            // Приоритет 6: Микрорайон (MicroDistrict)
            if (request.getMicroDistrict() != null && !request.getMicroDistrict().isEmpty()) {
                Expression<Object> microDistrictPriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(root.get("microDistrict"), request.getMicroDistrict()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(microDistrictPriority));
            }

            // Приоритет 16: Тип жилья (typeOfHousing)
            if (request.getTypeOfHousing() != null && !request.getTypeOfHousing().isEmpty()) {
                Expression<Object> housingTypePriority = criteriaBuilder.selectCase()
                        .when(root.get("typeOfHousing").in(request.getTypeOfHousing()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(housingTypePriority));
            }

            // Приоритет 7: Площадь (minArea и maxArea)
            if (request.getMinArea() != null && request.getMaxArea() != null) {
                Expression<Object> areaPriority = criteriaBuilder.selectCase()
                        .when(
                                criteriaBuilder.and(
                                        criteriaBuilder.greaterThanOrEqualTo(root.get("areaOfTheApartment"), request.getMinArea()),
                                        criteriaBuilder.lessThanOrEqualTo(root.get("areaOfTheApartment"), request.getMaxArea())
                                ),
                                1
                        )
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(areaPriority));
            }

            // Приоритет 8: Этаж (notTheFirstFloor)
            if (request.getNotTheFirstFloor() != null && request.getNotTheFirstFloor()) {
                Expression<Object> floorPriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.greaterThan(root.get("numberOfFloor"), 1), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(floorPriority));
            }

            // Приоритет 9: Верхний этаж (notTheTopFloor)
            if (request.getNotTheTopFloor() != null && request.getNotTheTopFloor()) {
                Expression<Object> topFloorPriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.lessThan(root.get("numberOfFloor"), root.get("maxFloorInTheBuilding")), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(topFloorPriority));
            }

            // Приоритет 10: Количество людей (numberOfPeopleAreYouAccommodating)
            if (request.getNumberOfPeopleAreYouAccommodating() != null) {
                Expression<Object> peoplePriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(root.get("numberOfPeopleAreYouAccommodating"), request.getNumberOfPeopleAreYouAccommodating()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(peoplePriority));
            }

            // Приоритет 11: Количество комнат (quantityOfRooms)
            if (request.getQuantityOfRooms() != null && !request.getQuantityOfRooms().isEmpty()) {
                // Если quantityOfRooms - это список, используем IN
                Expression<Object> roomsPriority = criteriaBuilder.selectCase()
                        .when(root.get("quantityOfRooms").in(request.getQuantityOfRooms()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(roomsPriority));
            }

            // Приоритет 12: Возраст (minAge и maxAge)
            if (request.getMinAge() != null && request.getMaxAge() != null) {
                Expression<Object> agePriority = criteriaBuilder.selectCase()
                        .when(
                                criteriaBuilder.and(
                                        criteriaBuilder.greaterThanOrEqualTo(root.get("minAge"), request.getMinAge()),
                                        criteriaBuilder.lessThanOrEqualTo(root.get("maxAge"), request.getMaxAge())
                                ),
                                1
                        )
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(agePriority));
            }

            // Приоритет 13: Разрешены ли питомцы (arePetsAllowed)
            if (request.getArePetsAllowed() != null) {
                Expression<Object> petsPriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(root.get("arePetsAllowed"), request.getArePetsAllowed()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(petsPriority));
            }

            // Приоритет 14: Включены ли коммунальные услуги (isCommunalServiceIncluded)
            if (request.getIsCommunalServiceIncluded() != null) {
                Expression<Object> communalServicePriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(root.get("isCommunalServiceIncluded"), request.getIsCommunalServiceIncluded()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(communalServicePriority));
            }

            // Приоритет 15: Предназначено ли для студентов (intendedForStudents)
            if (request.getIntendedForStudents() != null) {
                Expression<Object> studentsPriority = criteriaBuilder.selectCase()
                        .when(criteriaBuilder.equal(root.get("intendedForStudents"), request.getIntendedForStudents()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(studentsPriority));
            }

            // Приоритет 16: Тип жилья (typeOfHousing)
            if (request.getTypeOfHousing() != null && !request.getTypeOfHousing().isEmpty()) {
                Expression<Object> housingTypePriority = criteriaBuilder.selectCase()
                        .when(root.get("typeOfHousing").in(request.getTypeOfHousing()), 1)
                        .otherwise(0);
                orders.add(criteriaBuilder.desc(housingTypePriority));
            }

            // Дополнительные приоритеты можно добавить здесь

            // Дополнительная сортировка: например, по дате создания объявления
            orders.add(criteriaBuilder.desc(root.get("createdAt")));

            // Применение сортировки к запросу
            if (query != null) {
                query.orderBy(orders);
            }

            // 2. Нет фильтрации по приоритетным полям, все объявления включены
            return criteriaBuilder.conjunction(); // Возвращает "true", не ограничивая результаты
        });
    }

    @Transactional
    @Override
    public AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

        User user = userRepository.findByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(!announcement.getUser().equals(user)) {
            throw new IllegalArgumentException("You can't update this announcement");
        }

        if (announcement.getPhotos() != null) {
            announcement.getPhotos().clear();
        } else {
            announcement.setPhotos(new ArrayList<>());
        }

        if(!announcementRequest.getAddress().equals(announcement.getAddress())) {
            String[] coords = getCoordsFromAddress(announcementRequest.getAddress());
            announcement.setCoordsX(coords[0]);
            announcement.setCoordsY(coords[1]);
        }

        AnnouncementMapper.updateAnnouncementFromRequest(announcement, announcementRequest);

        List<Image> images = AnnouncementMapper.toImages(announcementRequest.getImages(), announcement);
        announcement.setPhotos(images);

        Announcement updatedAnnouncement = announcementRepository.save(announcement);

        return toAnnouncementResponse(updatedAnnouncement);
    }


    public AnnouncementResponse toAnnouncementResponse(Announcement announcement) {
        AnnouncementResponse response = modelMapper.map(announcement, AnnouncementResponse.class);

        response.setPhotos(announcement.getPhotos().stream()
                .map(AnnouncementMapper::toImageResponse)
                .collect(Collectors.toList()));
        response.setUser(AnnouncementMapper.toUserResponse(announcement.getUser()));

        return response;
    }

    @Override
    public AnnouncementResponseForAll toAnnouncementResponseForAll(Announcement announcement) {
        AnnouncementResponseForAll announcementResponseForAll = new AnnouncementResponseForAll();
        announcementResponseForAll.setAnnouncementId(announcement.getId());
        announcementResponseForAll.setImage(announcement.getPhotos().get(0).getUrl());
        announcementResponseForAll.setTitle(announcement.getTitle());
        announcementResponseForAll.setSelectedGender(announcement.getSelectedGender());
        announcementResponseForAll.setAddress(announcement.getAddress());
        announcementResponseForAll.setCost(announcement.getCost());
        announcementResponseForAll.setRoommates(announcement.getNumberOfPeopleAreYouAccommodating());
        announcementResponseForAll.setArriveDate(announcement.getArriveDate());
        announcementResponseForAll.setRoomCount(announcement.getQuantityOfRooms());
        announcementResponseForAll.setCost(announcement.getCost());
        return announcementResponseForAll;
    }
}

