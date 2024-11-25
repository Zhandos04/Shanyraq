package by.project.turamyzba.dto.responses;

import lombok.Data;

@Data
public class OptionDTO {
    private String text;

    public OptionDTO(String text) {
        this.text = text;
    }
}
