package romiinger.nailbook.Class;

public class Wallet {
    private String ammount;
    private String walletId;
    private String userId;

    public Wallet(Wallet wallet)
    {
        new Wallet(wallet.getAmmount(),wallet.getWalletId(),wallet.getUserId());
    }
    public Wallet(String ammount,String walletId,String userId)
    {
        this.ammount=ammount;
        this.walletId=walletId;
        this.userId=userId;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
