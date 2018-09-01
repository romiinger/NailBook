package romiinger.nailbook;

public class User
 {
    private String name;
    private String stId;
    private String phone;
    private String mail;

 public User(String name ,  String stId, String phone,String mail)
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
