package by.project.turamyzba.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResidentDataResponseDTO {
    private String name;
    private List<String> phoneNumbers;
    private ResidentType residentType;
}
