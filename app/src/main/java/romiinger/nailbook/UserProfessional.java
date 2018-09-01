package romiinger.nailbook;

public class UserProfessional extends User
{
    private WorkDiary agent;
    private Treatments treatments;

    public UserProfessional(String name ,  String stId, String phone,String mail)
    {
        super( name,stId,phone,mail);
        this.agent = new WorkDiary();
        this.treatments = new Treatments();
    }
}
