package beneficiary;

import java.io.Serializable;

public class Beneficiary  implements Serializable {
    private static final long serialVersionUID = 1L;
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
