package romiinger.nailbook;

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
    public void addUser(MyUser user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getStId());
        Map<String, Object> value = new HashMap<>();
        value.put("stId",user.getStId());
        value.put("name",user.getName());
        value.put("phone",user.getPhone());
        value.put("phone",user.getEmail());
        myRef.setValue(value);

        //myRef.setValue(student);
    }

    public interface GetSUserByIdListener{
        void onComplete(MyUser user);
    }
    public void getUserById(final String stId, final GetSUserByIdListener listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(stId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Student st = dataSnapshot.getValue(Student.class);
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                String stId = (String) value.get("stId");
                String name = (String) value.get("name");
                String phone = (String) value.get("phone");
                String email = (String) value.get("email");
                listener.onComplete(new MyUser(stId,name,phone,email));
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
                    String stId = (String) value.get("stId");
                    String name = (String) value.get("name");
                    String phone = (String) value.get("phone");
                    String email = (String) value.get("email");
                    data.add(new MyUser(name,phone,email,stId));
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
