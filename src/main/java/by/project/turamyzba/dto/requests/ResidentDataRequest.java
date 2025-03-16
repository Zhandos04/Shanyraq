package by.project.turamyzba.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class ResidentDataRequest {
    private String name;
    private List<String> phoneNumbers;
}
