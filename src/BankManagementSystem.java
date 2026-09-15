import java.util.Scanner;
import java.math.BigDecimal;

public class BankManagementSystem{

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        String pin = scan.nextLine();
        BigDecimal balance = scan.nextBigDecimal();

        Account acc1 = new Account(pin, balance);
        acc1.deposit(-500);
        acc1.withdraw(500);
    }
}