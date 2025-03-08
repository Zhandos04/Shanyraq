package by.project.turamyzba.dto.responses;

import lombok.Data;

@Data
public class OptionDTO {
    private Long optionId;
    private String text;

    public OptionDTO(Long optionId, String text) {
        this.optionId = optionId;
        this.text = text;
    }
}
