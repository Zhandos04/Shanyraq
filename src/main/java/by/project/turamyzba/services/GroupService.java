package by.project.turamyzba.services;

import by.project.turamyzba.dto.requests.GroupCreateDTO;

public interface GroupService {
    void createGroup(Long announcementId, GroupCreateDTO groupCreateDTO);
}
