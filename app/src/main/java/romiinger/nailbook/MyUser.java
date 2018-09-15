package romiinger.nailbook;

import com.firebase.ui.auth.data.model.User;

public class MyUser
 {
    private static String name;
    private static String stId;
    private static String phone;
    private static String mail;


    public MyUser(String userId)
    {
        this.stId= userId;
    }
 public MyUser(String name , String phone,String mail)
 {
     this.name= name;
     this.stId=stId;
     this.phone = phone;
     this.mail= mail;

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
 public String getMail()
 {
     return this.mail;
 }
}
