package romiinger.nailbook.Firebase;

import android.content.Intent;
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

import romiinger.nailbook.Class.MyUser;
import romiinger.nailbook.activitys.activity_user;


public class UserAdapterFirebase {
    private static final String TAG = "UserAdapterFirebase";
    private  static Boolean isNewUser=true;
    private  static FirebaseDatabase mdatabase;
    private static DatabaseReference myRef;

    public UserAdapterFirebase()
    {
         mdatabase = FirebaseDatabase.getInstance();
         Log.d(TAG,"mdatabase= " + mdatabase);

    }
    public interface AddUserListener{
        void onComplete();
    }
    public void addUser(MyUser user,final AddUserListener listener){

        if(user.getStId() == null){
            String stId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            user.setStId(stId);
        }

        myRef = mdatabase.getReference("users").child(user.getStId());

        Map<String, Object> value = new HashMap<>();
        value.put("name",user.getName());
        value.put("phone",user.getPhone());
        value.put("email",user.getEmail());
        value.put("stId",user.getStId());
        value.put("wallet",user.getWallet());
        myRef.setValue(value);
        listener.onComplete();

    }


    public interface GetUserByIdListener{
        void onComplete(MyUser user);
    }
    public static void getUserById(final String usId, final GetUserByIdListener listener){
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d(TAG,"usId= " +usId);
        final String userUid;
        if(usId == null){
            Log.d(TAG,"usId==null? " +usId);
            userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();}
        else
            userUid = usId;
        if(mdatabase.getReference("users").child(userUid)!= null)
             myRef = mdatabase.getReference("users").child(userUid);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"onDataChange");
                if(!dataSnapshot.exists()) {
                    listener.onComplete(new MyUser(userUid));
                    isNewUser = false;
                }
                else
                {
                    Log.d(TAG,"no new user ");
                    Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                    String id = (String) value.get("stId");
                    String name = (String) value.get("name");
                    String phone = (String) value.get("phone");
                    String email = (String) value.get("email");
                    String wallet = (String) value.get("wallet");
                    listener.onComplete(new MyUser(name,phone,email,id,wallet));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onComplete(null);

            }
        });
    }

    public interface GetUsersListener{
        void onComplete(List<MyUser> userList);
    }
    public void getUsers(final GetUsersListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        final List<MyUser> userList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();;
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    //Student st = dataSnapshot.getValue(Student.class);
                    Map<String, Object> value = (Map<String, Object>) snap.getValue();
                    String id = (String) value.get("stId");

                    String name = (String) value.get("name");
                    Log.d(TAG,"user name = "+ name + "user id=" + id);
                    String phone = (String) value.get("phone");
                    String email = (String) value.get("email");
                    String wallet = (String) value.get("wallet").toString();
                    MyUser newUser = new MyUser(name,phone,email,id,wallet);
                    userList.add(newUser);
                }
                for(int i=0; i<userList.size();i++)
                {
                    Log.d(TAG,"Client user i:" + i +"name:" + userList.get(i).getName());
                }
                listener.onComplete(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               //listener.onComplete(null);
            }
        });
    }

}
