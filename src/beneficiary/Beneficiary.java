package beneficiary;


public class Beneficiary {
    private String beneficiaryName;
    private String nickname;
    private String accountNumber;

    public Beneficiary(String name, String nickname, String account) {
         this.beneficiaryName = name;
         this.nickname = nickname;
         this.accountNumber = account;
    }

    public String getBeneficiaryName(){
        return this.beneficiaryName;
    }

     public String getNickname(){
        return this.nickname;
    }

     public String getAccountNumber(){
        return this.accountNumber;
    }


}
