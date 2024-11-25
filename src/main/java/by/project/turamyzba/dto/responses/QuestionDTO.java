package by.project.turamyzba.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private String text;
    private List<OptionDTO> options;

    public QuestionDTO( String text, List<OptionDTO> options) {
        this.text = text;
        this.options = options;
    }
}
