package romiinger.nailbook.Firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import romiinger.nailbook.Class.Wallet;

public class WalletAdapterFirebase {

    private static final String TAG = "WalletAdapterFirebase";
    private   FirebaseDatabase mdatabase;

    public WalletAdapterFirebase()
    {
        mdatabase = FirebaseDatabase.getInstance();
    }
    public String getNewWalletId()
    {
        DatabaseReference myRef = mdatabase.getReference("wallet");
        String walletId = myRef.push().getKey();
        Log.d(TAG,"walletId= " + walletId);
        return walletId;
    }
    public void addWallet(Wallet wallet){
        DatabaseReference myRef = mdatabase.getReference("wallet").child(wallet.getWalletId());
        Map<String, Object> value = new HashMap<>();
        value.put("ammount",wallet.getAmmount());
        value.put("walletId",wallet.getWalletId());
        value.put("userId",wallet.getUserId());
        myRef.setValue(value);
    }

    public interface GetWalletByClientIdListener{
        void onComplete(Wallet wallet);
    }

    public void getWalletByUserId(final String usId,final GetWalletByClientIdListener listener)
    {
        getWalletList(new WalletListListener() {
          public void onComplete(final List<Wallet> walletList)
          {
              for (int i=0;i<walletList.size();i++)
              {
                  Wallet wallet = walletList.get(i);
                  if(wallet.getUserId()==usId)
                  {
                      listener.onComplete(wallet);
                  }
              }
      }});

    }
    public interface WalletListListener{
        void onComplete(List<Wallet> walletList);
    }
    public void getWalletList(final WalletListListener listener)
    {
        DatabaseReference  myRef=mdatabase.getReference("wallet");
        final List<Wallet> walletList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                walletList.clear();;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Map<String, Object> value = (Map<String, Object>) snap.getValue();
                    String walletId = (String) value.get("walletId");
                    String ammount = (String) value.get("ammount");
                    String userId = (String) value.get("userId");
                    Wallet newUser = new Wallet(ammount,walletId,userId);
                    walletList.add(newUser);
                }
                for(int i=0; i<walletList.size();i++)
                {
                    Log.d(TAG,"Client user i:" + i +"name:" + walletList.get(i).getWalletId());
                }
                listener.onComplete(walletList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //listener.onComplete(null);
            }
        });
    }
}
