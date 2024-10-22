package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.AnnouncementRequest;
import by.project.turamyzba.mappers.AnnouncementMapper;
import by.project.turamyzba.models.Announcement;
import by.project.turamyzba.models.Image;
import by.project.turamyzba.repositories.AnnouncementRepository;
import by.project.turamyzba.services.AnnouncementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${2gis.api.key}")
    private String apiKey;

    @Value("${2gis.api.url}")
    private String apiUrl;

    @Autowired
    private AnnouncementRepository announcementRepository;

    public Announcement createAnnouncement(AnnouncementRequest announcementRequest) {
        String[] coords = getCoordsFromAddress(announcementRequest.getAddress());

        Announcement announcement = AnnouncementMapper.toEntity(announcementRequest, coords);
        List<Image> image = AnnouncementMapper.toImages(announcementRequest.getImageUrls(), announcement);
        announcement.setPhotos(image);
        return announcementRepository.save(announcement);
    }

    public Announcement updateAnnouncement(Long id, AnnouncementRequest announcementRequest) {
        //todo: добавить проверку на аутентификацию пользователя

        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Announcement not found"));
        AnnouncementMapper.updateAnnouncementFromRequest(announcement, announcementRequest);
        return announcementRepository.save(announcement);
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

