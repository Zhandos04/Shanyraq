package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.SavedFilterDTO;
import by.project.turamyzba.dto.responses.SavedFilterResponseDTO;
import by.project.turamyzba.entities.SavedFilter;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.exceptions.FilterNotFoundException;
import by.project.turamyzba.repositories.SavedFilterRepository;
import by.project.turamyzba.services.SavedFilterService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SavedFilterServiceImpl implements SavedFilterService {

    private final SavedFilterRepository savedFilterRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void saveFilter(SavedFilterDTO filterDTO) {
        // Извлекаем текущего пользователя из контекста безопасности (Spring Security)
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        // Создаем сущность SavedFilter на основе данных из DTO
        SavedFilter savedFilter = modelMapper.map(filterDTO, SavedFilter.class);
        savedFilter.setUser(user);

        savedFilterRepository.save(savedFilter);
    }

    @Override
    @Transactional
    public List<SavedFilterResponseDTO> deleteFilter(Long id) {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        Optional<SavedFilter> optionalSavedFilter = savedFilterRepository.findById(id);
        if (optionalSavedFilter.isEmpty()) {
            throw new FilterNotFoundException("Filter not found!");
        }
        SavedFilter savedFilter = optionalSavedFilter.get();
        if (!savedFilter.getUser().getId().equals(user.getId())) {
           throw new AccessDeniedException("it's not your saved filter!!!");
        }
        savedFilterRepository.delete(savedFilter);
        return savedFilterRepository.findByUser(user).stream().map(this::convertToSavedFilterResponse).collect(Collectors.toList());
    }
    private SavedFilterResponseDTO convertToSavedFilterResponse(SavedFilter savedFilter) {
        return modelMapper.map(savedFilter, SavedFilterResponseDTO.class);
    }
}

