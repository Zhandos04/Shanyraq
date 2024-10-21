package by.project.turamyzba.services;

import by.project.turamyzba.models.AnnouncementUser;
import by.project.turamyzba.models.User;
import by.project.turamyzba.repositories.AnnouncementUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementUserRepository announcementUserRepository;
    public List<AnnouncementUser> getUserAnnouncements(Long userId){
        return announcementUserRepository.findByUserId(userId);
    }
    public AnnouncementUser archiveAnnouncement(Long announcementId, User user){
        AnnouncementUser announcement = announcementUserRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("Обьявление не найдено"));
        announcement.setIsActive(false);
        return announcementUserRepository.save(announcement);
    }
//    public Announcement restoreAnnouncements(Long announcementId){
//        Announcement announcement = announcementRepository.findById(announcementId)
//                .orElseThrow(() -> new RuntimeException("Обьявление не найдено"));
//        announcement.setActive(true);
//        return announcementRepository.save(announcement);
//    }
//    public void deletedAnnouncement(Long announcementId){
//        Announcement announcement = announcementRepository.findById(announcementId)
//                .orElseThrow(() -> new RuntimeException("Обьявление не найдено"));
//        announcement.setDeleted(true);
//        announcementRepository.save(announcement);
//    }
}
