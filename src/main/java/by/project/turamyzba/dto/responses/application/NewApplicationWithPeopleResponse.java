package by.project.turamyzba.dto.responses.application;

import lombok.Data;

import java.util.List;

@Data
public class NewApplicationWithPeopleResponse {
    private List<EachPersonInNewApplicationWithPeopleResponse> people;
}
