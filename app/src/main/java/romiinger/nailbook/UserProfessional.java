package romiinger.nailbook;

import romiinger.nailbook.Class.MyUser;
import romiinger.nailbook.Class.Treatments;

public class UserProfessional extends MyUser
{

    private WorkDiary agent;
    private Treatments treatments;

    public UserProfessional(String name ,  String stId, String phone,String mail)
    {
        super( name,phone,mail);
        this.agent = new WorkDiary();
       // this.treatments = new Treatments();
    }
}
