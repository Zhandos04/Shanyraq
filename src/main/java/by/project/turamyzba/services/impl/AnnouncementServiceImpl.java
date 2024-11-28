package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.AnnouncementFilterRequest;
import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
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
        String[] coords = getCoordsFromAddress(announcementRequest.getMicroDistrict() + ", " + announcementRequest.getDistrict() + ", "
                + announcementRequest.getRegion());

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
    public Page<Announcement> searchRoommateListings(String search, Pageable pageable) {
        log.info("Searching roommate listings by search query: {}", search);
        return announcementRepository.findByTitleContainingAndIsDeletedFalse(search, pageable);
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

    @Transactional
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

        Announcement updatedAnnouncement = announcementRepository.save(announcement);

        return modelMapper.map(updatedAnnouncement, AnnouncementResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Announcement> getFilteredAnnouncements(AnnouncementFilterRequest request) {
        return announcementRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Handle null values and add conditions dynamically

            if (request.getAddress() != null && !request.getAddress().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("address"), request.getAddress()));
            }

            if (request.getStartAt() != null && !request.getStartAt().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("startAt"), request.getStartAt()));
            }

            if (request.getDeposit() != null) {
                predicates.add(criteriaBuilder.equal(root.get("deposit"), request.getDeposit()));
            }

            if (request.getSelectedGender() != null && !request.getSelectedGender().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("selectedGender"), request.getSelectedGender()));
            }

            if (request.getMonthlyExpensePerPerson() != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("monthlyExpensePerPerson"),
                        request.getMonthlyExpensePerPerson(),
                        request.getMonthlyExpensePerPerson() // You can add a range here if necessary
                ));
            }

            if (request.getIsLongTerm() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isLongTerm"), request.getIsLongTerm()));
            }

            if (request.getIsStudent() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isStudent"), request.getIsStudent()));
            }

            if (request.getAgeRange() != null) {
                predicates.add(criteriaBuilder.equal(root.get("ageRange"), request.getAgeRange()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public AnnouncementResponse toAnnouncementResponse(Announcement announcement) {
        AnnouncementResponse response = modelMapper.map(announcement, AnnouncementResponse.class);

        response.setPhotos(announcement.getPhotos().stream()
                .map(AnnouncementMapper::toImageResponse)
                .collect(Collectors.toList()));
        response.setUser(AnnouncementMapper .toUserResponse(announcement.getUser()));

        return response;
    }

}

