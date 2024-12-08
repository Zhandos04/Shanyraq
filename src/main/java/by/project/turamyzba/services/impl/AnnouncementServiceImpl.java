package by.project.turamyzba.services.impl;

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
import jakarta.persistence.criteria.Predicate;
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
import java.time.LocalDate;
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
    @Transactional
    public Page<AnnouncementResponseForAll> getAllRoommateListings(Pageable pageable) {
        return announcementRepository.findAllAnnouncementsDTO(
                pageable
        );
    }

    @Override
    @Transactional
    public Page<AnnouncementResponseForAll> searchRoommateListings(
            String region,
            String district,
            String microDistrict,
            Integer minPrice,
            Integer maxPrice,
            String gender,
            Integer roommatesCount,
            Pageable pageable
    ) {
        return announcementRepository.searchAnnouncementsDTO(
                region,
                district,
                microDistrict,
                minPrice,
                maxPrice,
                gender,
                roommatesCount,
                pageable
        );
    }

    @Override
    @Transactional
    public List<AnnouncementResponseForAll> getAllAnnouncementsForMap() {
        return announcementRepository.findAllForMap();
    }


    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponse getAnnouncementById(Long id) {
        log.info("Getting announcement by id: {}", id);
        return toAnnouncementResponse(announcementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Announcement not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseForAll> getUserAnnouncements() {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return announcementRepository.findAllActiveAnnouncementsByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseForAll> getUserArchiveAnnouncements() {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        return announcementRepository.findAllArchivedAnnouncementsByUser(user);
    }


    @Override
    @Transactional
    public void archiveAnnouncement(Long announcementId) throws BadRequestException {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("Обьявление не найдено"));
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if(announcement.getUser() == user) {
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

        if(!announcement.getIsDeleted() && announcement.getUser() == user){
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

        if(announcement.getIsArchived() && announcement.getUser() == user) {
            announcement.setIsDeleted(true);
        } else {
            throw new BadRequestException("bad request!");
        }
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> getFilteredAnnouncements(
            String selectedGender,
            String region,
            String district,
            String microDistrict,
            Integer minPrice,
            Integer maxPrice,
            Integer numberOfPeopleAreYouAccommodating,
            String quantityOfRooms,
            Integer minAge,
            Integer maxAge,
            String arriveData,
            Integer minArea,
            Integer maxArea,
            Boolean notTheFirstFloor,
            Boolean notTheTopFloor,
            Boolean arePetsAllowed,
            Boolean isCommunalServiceIncluded,
            Boolean intendedForStudents,
            String typeOfHousing,
            Boolean forALongTime
    ) {
        return announcementRepository.findAll((Specification<Announcement>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Фильтр по полу
            if (selectedGender != null && !selectedGender.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("selectedGender"), selectedGender));
            }

            // Фильтр по региону
            if (region != null && !region.isEmpty() && !region.equals("Весь Казахстан")) {
                predicates.add(criteriaBuilder.equal(root.get("region"), region));
            }

            // Фильтр по району
            if (district != null && !district.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("district"), district));
            }

            // Фильтр по микрорайону
            if (microDistrict != null && !microDistrict.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("microDistrict"), microDistrict));
            }

            // Фильтр по цене
            if (minPrice != null && maxPrice != null) {
                predicates.add(criteriaBuilder.between(root.get("cost"), minPrice, maxPrice));
            } else if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), minPrice));
            } else if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cost"), maxPrice));
            }

            // Фильтр по количеству человек
            if (numberOfPeopleAreYouAccommodating != null) {
                predicates.add(criteriaBuilder.equal(root.get("numberOfPeopleAreYouAccommodating"), numberOfPeopleAreYouAccommodating));
            }

            // Фильтр по количеству комнат
            if (quantityOfRooms != null && !quantityOfRooms.isEmpty()) {
                // Если подразумевается один вариант:
                // predicates.add(criteriaBuilder.equal(root.get("quantityOfRooms"), quantityOfRooms));

                // Если quantityOfRooms может быть списком значений,
                // то его можно преобразовать в список и использовать in():
                predicates.add(root.get("quantityOfRooms").in(quantityOfRooms));
            }

            // Фильтр по возрасту
            // Предполагаем, что мы ищем объявления, где возрастные ограничения объявления совпадают с заданными
            if (minAge != null && maxAge != null) {
                // Может быть как: квартира подходит, если её minAge <= заданный maxAge и её maxAge >= заданный minAge
                // тем самым проверяем пересечение возрастных диапазонов
                predicates.add(
                        criteriaBuilder.and(
                                criteriaBuilder.lessThanOrEqualTo(root.get("minAge"), maxAge),
                                criteriaBuilder.greaterThanOrEqualTo(root.get("maxAge"), minAge)
                        )
                );
            }

            // Фильтр по дате прибытия
            if (arriveData != null && !arriveData.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("arriveDate"), LocalDate.parse(arriveData)));
            }

            // Фильтр по площади
            if (minArea != null && maxArea != null) {
                predicates.add(criteriaBuilder.between(root.get("areaOfTheApartment"), minArea, maxArea));
            } else if (minArea != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("areaOfTheApartment"), minArea));
            } else if (maxArea != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("areaOfTheApartment"), maxArea));
            }

            // Фильтр по этажу (не первый этаж)
            if (notTheFirstFloor != null && notTheFirstFloor) {
                predicates.add(criteriaBuilder.greaterThan(root.get("numberOfFloor"), 1));
            }

            // Фильтр по верхнему этажу (не последний этаж)
            if (notTheTopFloor != null && notTheTopFloor) {
                predicates.add(
                        criteriaBuilder.lessThan(
                                root.get("numberOfFloor"),
                                root.get("maxFloorInTheBuilding")
                        )
                );
            }

            // Фильтр по питомцам
            if (arePetsAllowed) {
                predicates.add(criteriaBuilder.equal(root.get("arePetsAllowed"), true));
            }

            // Фильтр по коммунальным услугам
            if (isCommunalServiceIncluded) {
                predicates.add(criteriaBuilder.equal(root.get("isCommunalServiceIncluded"), true));
            }

            // Фильтр для студентов
            if (intendedForStudents) {
                predicates.add(criteriaBuilder.equal(root.get("intendedForStudents"), true));
            }

            // Фильтр по типу жилья
            if (typeOfHousing != null && !typeOfHousing.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("typeOfHousing"), typeOfHousing));
            }

            // Фильтр по длительности аренды
            if (forALongTime) {
                predicates.add(criteriaBuilder.equal(root.get("forALongTime"), true));
            }

            // Сортировка по дате создания (сначала новые)
            if (query != null) {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
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
            String[] coords = getCoordsFromAddress(announcementRequest.getRegion() + ", " + announcementRequest.getDistrict() + ", " + announcementRequest.getMicroDistrict() + ", " + announcementRequest.getAddress());
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
        response.setPhoneNumber(announcement.getUser().getPhoneNumber());
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
        announcementResponseForAll.setCoordsX(announcement.getCoordsX());
        announcementResponseForAll.setCoordsY(announcement.getCoordsY());
        return announcementResponseForAll;
    }
}

