package by.project.turamyzba.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long questionId;
    private String text;
    private List<OptionDTO> options;

    public QuestionDTO(Long questionId, String text, List<OptionDTO> options) {
        this.questionId = questionId;
        this.text = text;
        this.options = options;
    }
}
