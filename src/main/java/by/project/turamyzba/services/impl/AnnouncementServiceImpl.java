package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.dto.responses.AnnouncementResponse;
import by.project.turamyzba.mappers.AnnouncementMapper;
import by.project.turamyzba.models.Announcement;
import by.project.turamyzba.models.Image;
import by.project.turamyzba.models.User;
import by.project.turamyzba.repositories.AnnouncementRepository;
import by.project.turamyzba.repositories.UserRepository;
import by.project.turamyzba.services.AnnouncementService;
import by.project.turamyzba.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${2gis.api.key}")
    private String apiKey;

    @Value("${2gis.api.url}")
    private String apiUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest) {
        String[] coords = getCoordsFromAddress(announcementRequest.getAddress());
        User user =  userRepository.findByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("User: {}", user);

        Announcement announcement = AnnouncementMapper.toEntity(announcementRequest, coords);
        List<Image> images = AnnouncementMapper.toImages(announcementRequest.getImageUrls(), announcement);
        announcement.setPhotos(images);
        announcement.setUser(user);

        Announcement createdAnnouncement = announcementRepository.save(announcement);

        AnnouncementResponse announcementResponse = modelMapper.map(createdAnnouncement, AnnouncementResponse.class);
        log.info("Created Announcement: {}", announcementResponse);
        return announcementResponse;
    }

    @Transactional
    public AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

        User user =  userRepository.findByEmail(userService.getCurrentUser().getUsername())
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
        AnnouncementResponse announcementResponse = modelMapper.map(updatedAnnouncement, AnnouncementResponse.class);

        return announcementResponse;
    }

    private String[] getCoordsFromAddress(String address) {
        String[] coords = new String[2];
        String response = restTemplate.getForObject(apiUrl + "/geocode?q=" + address + "&fields=items.point&key=" + apiKey, String.class);
        log.info("Response from 2GIS API: {}", response);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            coords[0] = objectMapper.readTree(response).get("result").get("items").get(0).get("point").get("lon").asText();
            coords[1] = objectMapper.readTree(response).get("result").get("items").get(0).get("point").get("lat").asText();
        } catch (Exception e) {
            log.error("Error while parsing 2GIS API response: {}", e.getMessage());
        }

        return coords;
    }

 }

