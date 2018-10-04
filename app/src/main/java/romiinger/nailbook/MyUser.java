package romiinger.nailbook;

import com.firebase.ui.auth.data.model.User;

public class MyUser
 {
    private static String name;
    private static String stId;
    private static String phone;
    private static String email;
    public MyUser()
    {

    }
    public MyUser(MyUser user)
    {
        new MyUser(user.getName(),user.getPhone(),user.getEmail(),user.getStId());
    }

    public MyUser(String userId)
    {
        this.stId= userId;
    }

     public MyUser(String name , String phone,String email)
     {
         this.name= name;
         this.stId=stId;
         this.phone = phone;
         this.email= email;
     }
     public MyUser(String name , String phone,String email, String id)
     {
         this.name= name;
         this.stId=id;
         this.phone = phone;
         this.email= email;
     }
 public String getName()
 {
     return this.name;
 }
 public String getStId()
 {
     return this.stId;
 }

 public String getPhone()
 {
     return this.phone;
 }
 public String getEmail()
 {
     return this.email;
 }

}
