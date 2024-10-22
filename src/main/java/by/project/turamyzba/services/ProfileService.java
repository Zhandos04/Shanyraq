package by.project.turamyzba.services;

import by.project.turamyzba.dto.ProfileDTO;

public interface ProfileService {
    ProfileDTO getUser();
    void editProfile(ProfileDTO profileDTO);
}
