package by.project.turamyzba.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class GroupCreateDTO {
    private String name;
    private List<String> memberNames;
    private Integer capacity;
}
