package transaction;
import account.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.ArrayList;

public class Transaction {
    private TransactionType type;
    private String transactionID;
    private Account account;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private LocalDateTime dateTime;


    public Transaction(TransactionType type, Account acc, BigDecimal amount,
        BigDecimal balanceAfter, ArrayList<Transaction> allTransaction){
            this.transactionID=generateUniqueTransactionID(allTransaction);
            this.type=type;
            this.account=acc;
            this.amount=amount;
            this.balanceAfter=balanceAfter;
            this.dateTime= LocalDateTime.now();

        }

        public String getTransactionID(){   return this.transactionID;    }
        public TransactionType getTransactionType(){   return this.type;  }
        public BigDecimal getAmount(){ return this.amount;  }
        public BigDecimal getBalanceAfter(){ return this.balanceAfter;  }
        public LocalDateTime getDateTime(){ return this.dateTime; } 
    
        public String generateTransactionID() {
        Random rand = new Random();
        long number = 100000000L + (long) (rand.nextDouble() * 9000000000L);
        return "TXN" + number;
    }

        public String generateUniqueTransactionID(ArrayList<Transaction>trans){
        String transID;
        Boolean exist;
        do{
         transID= generateTransactionID();
         exist = false;
        for(Transaction t : trans){
            if(t.getTransactionID().equals(transID)){
                exist=true;
                break;
            }
        }
    }while(exist);
        return transID;
        }

        public void generateReceipt(){

        }

}
