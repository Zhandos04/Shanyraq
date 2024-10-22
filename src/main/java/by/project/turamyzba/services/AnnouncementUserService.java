package by.project.turamyzba.services;

import by.project.turamyzba.models.AnnouncementUser;
import by.project.turamyzba.repositories.AnnouncementUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementUserService {
    private final AnnouncementUserRepository announcementUserRepository;

    public List<AnnouncementUser> getUserAnnouncements(Long userId){
        return announcementUserRepository.findByUserId(userId);
    }

    public AnnouncementUser archiveAnnouncement(Long announcementId){
        AnnouncementUser announcementUser = announcementUserRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("Обьявление не найдено"));
        announcementUser.setIsActive(false);
        return announcementUserRepository.save(announcementUser);
    }

    public AnnouncementUser restoreAnnouncement(Long announcementId){
        AnnouncementUser announcementUser = announcementUserRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("Обьявление не найдено"));
        announcementUser.setIsActive(true);
        return announcementUserRepository.save(announcementUser);
    }

    public void deleteAnnouncement(Long announcementId){
        AnnouncementUser announcementUser = announcementUserRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("Обьявление не найдено"));
        if(!announcementUser.getIsActive()){
            announcementUser.setIsDeleted(true);
            announcementUserRepository.save(announcementUser);
        }
    }
}
