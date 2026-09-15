package exceptions;

public class AccountNotActiveException extends Exception{
    public AccountNotActiveException(String message){
        super(message);
    }
}