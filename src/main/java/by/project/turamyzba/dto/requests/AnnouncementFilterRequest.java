package by.project.turamyzba.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementFilterRequest {
    private String title;
    private String apartmentsInfo;
    private String address; // область/город
    private String startAt; // Время заезда (селект дата)
    private Integer deposit; // есть/нет депозита
    private Integer maxPeople; // кол-во людей
    private String selectedGender; // гендер
    private Boolean isCommunalServiceIncluded;
    private String roomiePreferences;
    private Integer monthlyExpensePerPerson; // ценовой диапазон
    private List<String> imageUrls;
    private Boolean isStudent; // студенты/не студенты
    private Boolean isLongTerm; // долгосрок/краткосрок
    private Integer ageRange; // возрастной
}
