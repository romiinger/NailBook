package romiinger.nailbook.Class;

import android.util.Log;

import romiinger.nailbook.Firebase.WalletAdapterFirebase;

public class Wallet {

    private static final String TAG = "Wallet";

    private String ammount;
    private String walletId;
    private String userId;
    private WalletAdapterFirebase walletAdapterFirebase;

    public Wallet(String userId){
        walletAdapterFirebase = new WalletAdapterFirebase();
        this.walletId = walletAdapterFirebase.getNewWalletId();
        this.userId = userId;
        this.ammount = "0";
        walletAdapterFirebase.addWallet(this);
    }

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
        int newAmmount = Integer.parseInt(ammount) + Integer.parseInt(this.ammount);
        Log.d(TAG,"the new ammount is : "+ newAmmount);
        this.ammount = Integer.toString(newAmmount);
        walletAdapterFirebase = new WalletAdapterFirebase();
        walletAdapterFirebase.addWallet(this);
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
