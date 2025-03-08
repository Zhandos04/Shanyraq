package by.project.turamyzba.services;

import by.project.turamyzba.dto.requests.PasswordDTO;
import by.project.turamyzba.dto.requests.ProfileDTO;
import by.project.turamyzba.dto.responses.ProfileResponse;
import by.project.turamyzba.dto.responses.ProfileWithFiltersResponse;

public interface ProfileService {
    ProfileResponse getUser();
    ProfileResponse editProfile(ProfileDTO profileDTO);
    void updatePassword(PasswordDTO passwordDTO);

    void uploadProfilePhoto(String fileUrl);

    void addPassword(String password);

    ProfileWithFiltersResponse getUserWithFilters();
}
