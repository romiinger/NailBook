package romiinger.nailbook;

import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class UserAdapter
{
    private static MyUser mUser;
    private static final String TAG = "UserAdapter";
    private static FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private static DatabaseReference mref,userRef;
    private static String muserUid;
    private static Boolean isNewUser=true;
    public static Boolean getUserProfile()
    {
        userRef= FirebaseDatabase.getInstance().getReference().child("users");
        Log.d(TAG,"mref= " + userRef );
        muserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG,"muserUid= " + muserUid );
        mref =userRef.child(muserUid);
        Log.d(TAG,"mref= " + mref );
        mref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()) {
                    Log.d(TAG,"user not exist");
                    mUser= new MyUser(muserUid);
                    //userRef = userRef.push();
                    //userRef.setValue(mUser.getStId());
                    isNewUser = false;
                }
                else
                {
                    isNewUser = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

       return isNewUser;
    }

    public static void setUserProfile()
    {

        Log.d(TAG,"in setUserProfile()");
        Log.d(TAG,"mUser.getStId()= "+mUser.getStId());
        mref.child(mUser.getStId()).setValue(mUser);
    }
    /*
    private static void checkAdmin(String uid) {
        FirebaseUtil.isAdmin = false;
        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrators").child(uid);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
                Log.d(TAG, "you are in administrator");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }
*/
  /*
    public static boolean isIsAdmin() {
        return isAdmin;
    }
    */
}
