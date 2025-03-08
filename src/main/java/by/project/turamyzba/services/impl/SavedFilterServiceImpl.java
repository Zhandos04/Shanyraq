package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.SavedFilterDTO;
import by.project.turamyzba.entities.SavedFilter;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.repositories.SavedFilterRepository;
import by.project.turamyzba.services.SavedFilterService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavedFilterServiceImpl implements SavedFilterService {

    private final SavedFilterRepository savedFilterRepository;
    private final UserService userService;

    @Override
    public void saveFilter(SavedFilterDTO filterDTO) {
        // Извлекаем текущего пользователя из контекста безопасности (Spring Security)
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        // Создаем сущность SavedFilter на основе данных из DTO
        SavedFilter savedFilter = SavedFilter.builder()
                .selectedGender(filterDTO.getSelectedGender())
                .region(filterDTO.getRegion())
                .district(filterDTO.getDistrict())
                .microDistrict(filterDTO.getMicroDistrict())
                .minPrice(filterDTO.getMinPrice())
                .maxPrice(filterDTO.getMaxPrice())
                .numberOfPeopleAreYouAccommodating(filterDTO.getNumberOfPeopleAreYouAccommodating())
                .quantityOfRooms(filterDTO.getQuantityOfRooms())
                .minAge(filterDTO.getMinAge())
                .maxAge(filterDTO.getMaxAge())
                .arriveDate(filterDTO.getArriveDate())
                .minArea(filterDTO.getMinArea())
                .maxArea(filterDTO.getMaxArea())
                .notTheFirstFloor(filterDTO.getNotTheFirstFloor())
                .notTheTopFloor(filterDTO.getNotTheTopFloor())
                .arePetsAllowed(filterDTO.getArePetsAllowed())
                .isCommunalServiceIncluded(filterDTO.getIsCommunalServiceIncluded())
                .intendedForStudents(filterDTO.getIntendedForStudents())
                .typeOfHousing(filterDTO.getTypeOfHousing())
                .forALongTime(filterDTO.getForALongTime())
                .user(user)
                .build();

        savedFilterRepository.save(savedFilter);
    }
}

