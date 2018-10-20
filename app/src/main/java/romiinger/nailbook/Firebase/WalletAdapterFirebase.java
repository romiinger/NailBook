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
    private FirebaseDatabase mdatabase;

    public WalletAdapterFirebase() {
        mdatabase = FirebaseDatabase.getInstance();
    }

    public String getNewWalletId() {
        DatabaseReference myRef = mdatabase.getReference("wallet");
        String walletId = myRef.push().getKey();
        Log.d(TAG, "walletId= " + walletId);
        return walletId;
    }

    public void addWallet(Wallet wallet) {
        DatabaseReference myRef = mdatabase.getReference("wallet").child(wallet.getWalletId());
        Map<String, Object> value = new HashMap<>();
        value.put("ammount", wallet.getAmmount());
        value.put("walletId", wallet.getWalletId());
        value.put("userId", wallet.getUserId());
        myRef.setValue(value);
    }

    public interface GetWalletByClientIdListener {
        void onComplete(Wallet wallet);
    }

    public void getWalletByUserId(final String usId, final GetWalletByClientIdListener listener) {
        getWalletList(new WalletListListener() {
            public void onComplete(final List<Wallet> walletList) {
                Log.d(TAG, "after get wallet list, wallet size=" + walletList.size());
                String userId = usId;
                if (userId == null) {
                    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                }
                Log.d(TAG, "search userId = " + userId);
                for (int i = 0; i < walletList.size(); i++) {
                    Wallet wallet = walletList.get(i);
                    Log.d(TAG, "wallet i: " + i + " userId= " + wallet.getUserId());
                    if (wallet.getUserId().equals(userId)) {
                        Log.d(TAG, "Wallet by userId found ! ");
                        listener.onComplete(wallet);
                    }
                }
            }
        });

    }
    public void getWalletByUserIdNoView(final String usId, final GetWalletByClientIdListener listener) {
        getWalletListNoView(new WalletListListener() {
            public void onComplete(final List<Wallet> walletList) {
                        Wallet wallet = foundWallet(walletList,usId);
                        listener.onComplete(wallet);

            }
        });
    }
    private Wallet foundWallet( List<Wallet> walletList,  String uid)
    {
        Log.d(TAG, "after get wallet list, wallet size=" + walletList.size());
        if (uid == null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        Log.d(TAG, "search userId = " + uid);
        for (int i = 0; i < walletList.size(); i++) {
            Wallet wallet = walletList.get(i);
            Log.d(TAG, "wallet i: " + i + " userId= " + wallet.getUserId());
            if (wallet.getUserId().equals(uid)) {
                Log.d(TAG, "Wallet by userId found ! ");
                return wallet;
            }
        }
        return null;
    }

    public interface WalletListListener {
        void onComplete(List<Wallet> walletList);
    }

    public void getWalletListNoView(final WalletListListener listener) {
        DatabaseReference myRef = mdatabase.getReference("wallet");
        final List<Wallet> walletList = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                walletList.clear();
                walletList.addAll(toWallet(dataSnapshot));
                listener.onComplete(walletList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //listener.onComplete(null);
            }
        });
    }

    public void getWalletList(final WalletListListener listener) {
        DatabaseReference myRef = mdatabase.getReference("wallet");
        final List<Wallet> walletList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                walletList.clear();
                walletList.addAll(toWallet(dataSnapshot));
                listener.onComplete(walletList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //listener.onComplete(null);
            }
        });
    }

    private List<Wallet> toWallet(DataSnapshot dataSnapshot) {
        List<Wallet> walletList = new ArrayList<>();
        for (DataSnapshot snap : dataSnapshot.getChildren()) {
            Map<String, Object> value = (Map<String, Object>) snap.getValue();
            String walletId = value.get("walletId").toString();
            String ammount = value.get("ammount").toString();
            String userId = value.get("userId").toString();
            Wallet newWallet = new Wallet(ammount, walletId, userId);
            walletList.add(newWallet);
        }
        return walletList;
    }
}