import java.util.Scanner;
import java.math.BigDecimal;
import account.*;
import exceptions.AccountNotActiveException;
import exceptions.InvalidAmountException;
import exceptions.InvalidPinException;

public class BankManagementSystem{

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        String pin = scan.nextLine();
        BigDecimal balance = scan.nextBigDecimal();
         Account acc1;

       try{
        acc1 = new Account(pin, balance);
        System.out.println("Account created sucessfully");
       } catch(InvalidPinException e){
        System.out.println("Account creation failed: " + e.getMessage());
        return;      
    }


       try{
        acc1.deposit(new BigDecimal(-500));
         System.out.println("Amount deposited sucessfully");

       }catch(InvalidAmountException| AccountNotActiveException e){
         System.out.println("Deposit failed: " + e.getMessage());

       }
        
    }
}