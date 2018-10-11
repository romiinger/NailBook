package romiinger.nailbook.Class;

public class MyUser
 {
    private  String name;
    private  String stId;
    private  String phone;
    private  String email;
    private  String wallet;
    public MyUser()
    {

    }
    public MyUser(MyUser user)
    {
        new MyUser(user.getName(),user.getPhone(),user.getEmail(),user.getStId(),user.getWallet());
    }

    public MyUser(String userId)
    {
        this.stId= userId;
        this.wallet="0";
    }

     public MyUser(String name , String phone,String email)
     {
         this.name= name;
         this.phone = phone;
         this.email= email;
     }
     public MyUser(String name , String phone,String email, String id, String wallet)
     {
         this.name= name;
         this.stId=id;
         this.phone = phone;
         this.email= email;
         this.wallet = wallet;
     }

     public  String getWallet() {
         return wallet;
     }

     public void setWallet(String wallet) {
         this.wallet = wallet;
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
