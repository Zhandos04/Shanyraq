package by.project.turamyzba.dto.requests;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementRequest {

    @NotNull(message = "Title cannot be null")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotNull(message = "Apartments info cannot be null")
    @Size(min = 10, max = 500, message = "Apartments info must be between 10 and 500 characters")
    private String apartmentsInfo;

    @NotNull(message = "Address cannot be null")
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    private String address;

    @NotNull(message = "Start date cannot be null")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Start date must be in the format YYYY-MM-DD")
    private String startAt;

    @NotNull(message = "Deposit cannot be null")
    @Positive(message = "Deposit must be positive")
    private Integer deposit;

    @NotNull(message = "Max people cannot be null")
    @Min(value = 1, message = "At least 1 person must be allowed")
    @Max(value = 10, message = "No more than 10 people allowed")
    private Integer maxPeople;

    @NotNull(message = "Selected gender cannot be null")
    @Pattern(regexp = "Male|Female|Any", message = "Gender must be either Male, Female or Any")
    private String selectedGender;

    @NotNull(message = "Communal service inclusion status cannot be null")
    private Boolean isCommunalServiceIncluded;

    @NotNull(message = "Roomie preferences cannot be null")
    @Size(min = 5, max = 200, message = "Roomie preferences must be between 5 and 200 characters")
    private String roomiePreferences;

    @NotNull(message = "Monthly expense per person cannot be null")
    @Positive(message = "Monthly expense per person must be positive")
    private Integer monthlyExpensePerPerson;

    private List<String> imageUrls;
}
