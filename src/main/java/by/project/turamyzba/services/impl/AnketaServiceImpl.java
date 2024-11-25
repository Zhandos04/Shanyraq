package by.project.turamyzba.services.impl;

import by.project.turamyzba.dto.requests.AnketaDTO;
import by.project.turamyzba.entities.Anketa;
import by.project.turamyzba.entities.User;
import by.project.turamyzba.repositories.AnketaRepository;
import by.project.turamyzba.services.AnketaService;
import by.project.turamyzba.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnketaServiceImpl implements AnketaService {
    private final UserService userService;
    private final AnketaRepository anketaRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public void saveAnswers(List<AnketaDTO> anketaDTO) {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        List<Anketa> anketaList = anketaDTO.stream()
                .map(dto -> convertToAnketa(dto, user))
                .toList();

        anketaRepository.saveAll(anketaList);
    }
    @Override
    public List<AnketaDTO> getAnswers() {
        User user = userService.getUserByEmail(userService.getCurrentUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        return anketaRepository.findAllByUser(user).stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    private Anketa convertToAnketa(AnketaDTO anketaDTO, User user) {
        Anketa anketa = new Anketa();
        anketa.setUser(user);
        anketa.setNumberOfQuestion(anketaDTO.getNumberOfQuestion());
        anketa.setAnswerText(anketaDTO.getAnswerText());
        return anketa;
    }
    private AnketaDTO convertToDTO(Anketa anketa) {
        return modelMapper.map(anketa, AnketaDTO.class);
    }
}
