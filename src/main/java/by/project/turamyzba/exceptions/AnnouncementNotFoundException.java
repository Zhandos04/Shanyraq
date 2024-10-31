package by.project.turamyzba.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnnouncementNotFoundException extends RuntimeException{
    public AnnouncementNotFoundException(String message) {
        super(message);
    }
}
