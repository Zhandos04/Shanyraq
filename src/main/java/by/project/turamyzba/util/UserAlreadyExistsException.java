package by.project.turamyzba.util;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String msg){
        super(msg);
    }
}