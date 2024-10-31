package by.project.turamyzba.services;

import by.project.turamyzba.dto.requests.ProfileDTO;

public interface ProfileService {
    ProfileDTO getUser();
    void editProfile(ProfileDTO profileDTO);
}
