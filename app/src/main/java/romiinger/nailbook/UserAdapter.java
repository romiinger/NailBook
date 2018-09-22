package romiinger.nailbook;

import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserAdapter
{
    private static MyUser mUser;
    private static final String TAG = "UserAdapter";
    private static FirebaseUtil mFirebaseUtil=new FirebaseUtil();
    private static DatabaseReference mref,userRef;
    private static String muserUid;
    private static Boolean isNewUser;
    public UserAdapter()
    {
        this.isNewUser = true;
        getUserProfile();
        Log.d(TAG,"after getUserProfile initialize, mUser.getName=" + mUser.getName());
    }

    public static void getUserProfile()
    {
        Log.d(TAG,"in getUserProfile() ");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        muserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mref = userRef.child(muserUid).child(muserUid);
        Log.d(TAG,"before event listener");
        ValueEventListener postListener = new ValueEventListener()
         {
           // MyUser newUser;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"onDataChange");
                if(!dataSnapshot.exists()) {
                    mUser = new MyUser(muserUid);
                     isNewUser = false;
                }
                else
                {
                    Log.d(TAG,"no new user ");
                    //mUser = dataSnapshot.getValue(MyUser.class);
                    Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                    String email = (String) value.get("mail");
                    String name = (String) value.get("name");
                    String phone = (String) value.get("phone");
                    String id = (String) value.get("stId");
                    MyUser userFromDb = new MyUser( name, phone,email,id);
                    //UserAdapter.setUserProfile(userFromDb);
                    mUser= new MyUser(userFromDb);
                    Log.d(TAG,"mUser.getName()" + mUser.getName());
                    isNewUser = true;
                    notifyAll();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        mref.addValueEventListener( postListener);

        Log.d(TAG,"after event listener");
        //mUser= new MyUser();
    }

    public static void setUserProfile(MyUser user)
    {
        Log.d(TAG,"in setUserProfile()");
        Log.d(TAG,"mUser.getStId()= "+ getmUser().getStId());
        mref.child(getmUser().getStId()).child(getmUser().getStId()).setValue(getmUser());
    }
     public static Boolean isNewUser()
     {
         return isNewUser;
     }

    public static MyUser getmUser() {
        Log.d(TAG, "in getmUser(), mUser.stID="+ mUser.getStId());
        return mUser;
    }

    public static void setmUser(MyUser mUser) {
        UserAdapter.mUser = mUser;
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
