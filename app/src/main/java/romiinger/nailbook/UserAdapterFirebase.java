package romiinger.nailbook;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class UserAdapterFirebase {
    private static final String TAG = "UserAdapterFirebase";
    private  Boolean isNewUser=true;
    private  FirebaseDatabase mdatabase;
    private DatabaseReference myRef;

    public UserAdapterFirebase()
    {
         mdatabase = FirebaseDatabase.getInstance();
    }
    public void addUser(MyUser user){

        myRef = mdatabase.getReference("users").child(user.getStId());

        Map<String, Object> value = new HashMap<>();
        value.put("name",user.getName());
        value.put("phone",user.getPhone());
        value.put("email",user.getEmail());
        value.put("stId",user.getStId());
        myRef.setValue(value);

        //myRef.setValue(student);
    }


    public interface GetUserByIdListener{
        void onComplete(MyUser user);
    }
    public void getUserById(final GetUserByIdListener listener){
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                    String id = (String) value.get("id");
                    String name = (String) value.get("name");
                    String phone = (String) value.get("phone");
                    String email = (String) value.get("email");
                    listener.onComplete(new MyUser(name,phone,email,id));
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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinkedList<MyUser> data = new LinkedList<>();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    //Student st = dataSnapshot.getValue(Student.class);
                    Map<String, Object> value = (Map<String, Object>) snap.getValue();
                    String id = (String) value.get("id");
                    String name = (String) value.get("name");
                    String phone = (String) value.get("phone");
                    String email = (String) value.get("email");
                    data.add(new MyUser(name,phone,email,id));
                }
                listener.onComplete(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onComplete(null);
            }
        });
    }

}
