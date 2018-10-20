package romiinger.nailbook.Class;


import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import romiinger.nailbook.Firebase.AppointmentAdapterFirebase;
import romiinger.nailbook.Firebase.LimitationAdapterFirebase;
import romiinger.nailbook.Firebase.TreatmentsAdapterFirebase;
import romiinger.nailbook.Firebase.WalletAdapterFirebase;
import romiinger.nailbook.Firebase.WorkDayAdapterFirebase;
import romiinger.nailbook.activitys.Calendar.AppointmentActivity;
import romiinger.nailbook.activitys.Calendar.NewAppointmentActivity;


public class ScheduleAppointment {
    private static final String TAG = "ScheduleAppointment";
    private Calendar cal ;
    private DateFormat mdf = new SimpleDateFormat("HH:mm");
    private  DateFormat formatDate = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
    private WorkDayAdapterFirebase workDayAdapterFirebase;
    private AppointmentAdapterFirebase appointmentAdapterFirebase;
    private TreatmentsAdapterFirebase treatmentsAdapterFirebase;
    private WalletAdapterFirebase walletAdapterFirebase;
    private LimitationAdapterFirebase limitationAdapterFirebase;
    private WorkDay mWorkday;
    private List<Appointment> mAppointmentList;
    private Treatments mTreatment;
    private Date mEndAppointmnet;
    private Wallet mWallet;

    public ScheduleAppointment() {
        workDayAdapterFirebase = new WorkDayAdapterFirebase();
        appointmentAdapterFirebase = new AppointmentAdapterFirebase();
        treatmentsAdapterFirebase = new TreatmentsAdapterFirebase();
        walletAdapterFirebase = new WalletAdapterFirebase();
        limitationAdapterFirebase = new LimitationAdapterFirebase();
    }
    public void appendAppointmnetToClient(final Appointment appointment,final Treatments treatment,
                                          final GetSchechuleListener listener)    {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        walletAdapterFirebase.getWalletByUserIdNoView(appointment.getClientId(), new WalletAdapterFirebase.GetWalletByClientIdListener() {
            @Override
            public void onComplete(final Wallet wallet) {
                mWallet =wallet;

                int walletAmmount = Integer.parseInt(mWallet.getAmmount());
                int treatmentPrice = Integer.parseInt(treatment.getPrice());

                if(!(walletAmmount < treatmentPrice)) {
                    Log.d(TAG, "wallet have sufficient amoount to sabe this appointmnet");
                    walletAmmount = walletAmmount - treatmentPrice;
                    treatmentPrice= treatmentPrice * -1;
                    String newWalletValue = Integer.toString(walletAmmount);
                    String streatmentPrice = Integer.toString(treatmentPrice);
                    Log.d(TAG, "The new ammount value is:  " +newWalletValue);
                    mWallet.setAmmount(streatmentPrice);
                    appointment.setClientId(uid);
                    if (treatment.getId() != appointment.getTreatmentId()) {
                        appointment.setTreatmentId(treatment.getId());
                        Date startAppointment = toTime(appointment.getStartHour());
                        Date endAppintment = addMinutesToTime(treatment.getDuration(), startAppointment);
                        appointment.setEndHour(mdf.format(endAppintment));
                    }
                    appointmentAdapterFirebase.addAppointment(appointment, new AppointmentAdapterFirebase.GetAddAppointmentListener() {
                        @Override
                        public void onComplete(final boolean onSucess) {
                            listener.onComplete(onSucess);
                            updateAfterAppendAppointment(appointment.getDate());
                        }
                    });
                }

                else
                {
                    Toast.makeText(NewAppointmentActivity.getAppContext(),"no fount ammount in her wallet to save appointments,contact your Tecnical Nails",Toast.LENGTH_SHORT).show();
                    listener.onComplete(false);
                }
            }});


    }
    public void getEmptyAppointments(final WorkDay workDay, final GetDatesToAppointmentListener listener) {
        Log.d(TAG, "in getEmptyAppointments() ");
        treatmentsAdapterFirebase.getMaxDurationTreatment(
                new TreatmentsAdapterFirebase.GetMaxDurationTreatmentListener() {
                    @Override
                    public void onComplete(final Treatments maxDurationTreatment) {

                        if (workDay.getDate().isEmpty() || workDay.getDate() == null) {
                            Log.d(TAG, "no workday exist in this date");
                            listener.onComplete(null);
                        } else {
                            mWorkday = workDay;
                            Date openHour = toTime(mWorkday.getOpenHour());
                            Log.d(TAG, "open workday = " + openHour);
                            Date clouseHour = toTime(mWorkday.getCloseHour());
                            Log.d(TAG, "clouse workday = " + clouseHour);
                            if (openHour == null || clouseHour == null) {
                                Log.d(TAG, "failed to cast string date to date");
                                listener.onComplete(null);
                            }
                            final List<Appointment> emptyAppointments = new ArrayList<>();
                            Date hourAppointment = openHour;
                            String duration = maxDurationTreatment.getDuration();
                            Date endAppointment = addMinutesToTime(duration, hourAppointment);
                            Log.d(TAG, "endAppointment = " + endAppointment);
                            while (!(endAppointment.compareTo(clouseHour) > 0)) {
                                addNewAppointment(workDay.getDate(), hourAppointment, endAppointment, maxDurationTreatment.getId());
                                hourAppointment = endAppointment;
                                endAppointment = addMinutesToTime(duration, hourAppointment);
                            }
                            listener.onComplete(emptyAppointments);
                        }
                    }


                });
    }
    public void veridicationAppointments(final LimitationEvent limitationEvent,
                                         final GetVeridicationLimitation listener) {

        Log.d(TAG, "in veridicationAppointments() ");
        Log.d(TAG, "before getClientAppointmnetByDate() " + limitationEvent.getDate());
        appointmentAdapterFirebase.getClientAppointmnetByDate(limitationEvent.getDate(),
                new AppointmentAdapterFirebase.GetAppointmentsListListener() {
                    @Override
                    public void onComplete(List<Appointment> appointmentList) {
                        boolean verificator = false;
                        for (int i = 0; i < appointmentList.size(); i++) {
                            isOverlap(limitationEvent, appointmentList.get(i), new GetVeridicationLimitation() {
                                @Override
                                public void onComplete(boolean isVerificaded) {
                                    if (isVerificaded) {
                                        Log.d(TAG, "no authorize no add this limitation");
                                        listener.onComplete(false);

                                    } else {
                                        Log.d(TAG, " authorize to add this limitation");
                                        listener.onComplete(true);
                                    }
                                }
                            });
                        }
                        if(appointmentList.size()==0)
                        {
                            Log.d(TAG, " authorize to add this limitation");
                            listener.onComplete(true);
                        }
                    }
                });


    }
    public void updateAfterAppendAppointment(final String date)    {
        appointmentAdapterFirebase.getAppointmentsByDateNoView(date, new AppointmentAdapterFirebase.GetAppointmentsListListener() {
            @Override
            public void onComplete(List<Appointment> appointmentList) {
                unionLists(appointmentList, date, new GetUnionListListener() {
                    @Override
                    public void onComplete(List<MyEventCalendar> list) {
                        if(list.size()==0)
                        {
                            Log.d(TAG,"error to union lists");
                            return;
                        }
                        compactList(list,date);
                    }
                });

            }});


    }
    public void updateEmptyAppointments(final LimitationEvent limitationEvent) {
        Log.d(TAG, "in updateEmptyAppointments() ");
        //remove emptys appointmenys overlap to limitaion event
        appointmentAdapterFirebase.getEmptyAppointmentsByDateNoView(limitationEvent.getDate(),
                new AppointmentAdapterFirebase.GetAppointmentsListListener() {
                    @Override
                    public void onComplete(final List<Appointment> appointmentList) {
                        for (int i = 0; i < appointmentList.size(); i++) {
                            final int index = i;
                            isOverlap(limitationEvent, appointmentList.get(i), new GetVeridicationLimitation() {
                                @Override
                                public void onComplete(boolean isVerificaded) {
                                    if (isVerificaded) {
                                        appointmentAdapterFirebase.removeApoointmentById(appointmentList.get(index));
                                        appointmentList.remove(index);
                                        Log.d(TAG, "empty appointment is removed");
                                    }
                                }
                            });
                            unionLists(appointmentList, limitationEvent.getDate(), new GetUnionListListener() {
                                @Override
                                public void onComplete(List<MyEventCalendar> list) {
                                    compactList(list,limitationEvent.getDate());
                                }
                            });
                        }
                    }
                });
    }
    public Date toTime(String stringTime) {
        Date date = new Date();
        try {
            date = new Time(mdf.parse(stringTime).getTime());
            return date;
        } catch (Exception e) {
            Log.e(TAG, "Exception" + e);
            return null;
        }
    }
    public void removeClientAppointment(final Appointment appointment, final GetSchechuleListener listener){
        cal= Calendar.getInstance();
        Date todayDate = cal.getTime();
        String sDate = appointment.getDate();
        Date appointmentDate = null;
        try{
             appointmentDate = formatDate.parse(sDate);
        }
        catch (Exception e){
            Log.e(TAG,"Exception " + e);
        }
        if(appointmentDate!=null )
        {
            if (todayDate.compareTo(appointmentDate)< 0)
            {
                final String clientId = appointment.getClientId();
                appointment.setClientId(null);
                appointmentAdapterFirebase.addAppointment(appointment,
                        new AppointmentAdapterFirebase.GetAddAppointmentListener() {
                            @Override
                            public void onComplete(boolean onSucess) {
                                if (onSucess) {
                                    Log.d(TAG, "remove clientId to appointment is success");
                                    refoundWallet(clientId,appointment.getTreatmentId(),listener);

                                } else {
                                    Log.d(TAG, "remove clientId to appointment is failed ");
                                    listener.onComplete(false);
                                }
                            }
                        });
            }
            else
            {
                Toast.makeText(AppointmentActivity.getAppContext(),"remove appointment no allowed in the same day",Toast.LENGTH_SHORT).show();
                listener.onComplete(false);
            }

        }

    }


    private void refoundWallet(final String clientId, String treatmentId, final GetSchechuleListener listener){
        treatmentsAdapterFirebase.getTreatmentById(treatmentId, new TreatmentsAdapterFirebase.GetTreatmentByIdListener() {
            @Override
            public void onComplete(Treatments treatment) {
                final String price =treatment.getPrice();
                walletAdapterFirebase.getWalletByUserIdNoView(clientId, new WalletAdapterFirebase.GetWalletByClientIdListener() {
                    @Override
                    public void onComplete(Wallet wallet) {
                        wallet.setAmmount(price);
                        listener.onComplete(true);
                    }
                });
            }
        });

    }
    private void isOverlap(final LimitationEvent limitationEvent, final Appointment appointment, final GetVeridicationLimitation listener) {

        Log.d(TAG, "in isOverlap() ");
        final Date startLimtationTime = toTime(limitationEvent.getStartHour());
        final Date endLimationTime = toTime(limitationEvent.getEndHour());
        if (startLimtationTime == null || endLimationTime == null) {
            Log.e(TAG, "Errror to parse string to time date in toTime");
            listener.onComplete(false);
        }
        Date startAppointment = toTime(appointment.getStartHour());
        Date endAppointment = toTime(appointment.getEndHour());

        if (startLimtationTime.compareTo(startAppointment) == 0 ||
                (startLimtationTime.compareTo(startAppointment) < 0 &&
                        !(startAppointment.compareTo(endLimationTime) > 0)) ||
                (startAppointment.compareTo(startLimtationTime)) < 0 &&
                        (startLimtationTime.compareTo(endAppointment) < 0)) {
            Log.d(TAG, "this empty appointment is overlap to limitaion event");
            listener.onComplete(true);
        } else {
            listener.onComplete(false);
        }


    }
    private void unionLists(final List<Appointment> appointmentList,String date,final GetUnionListListener listener ) {
        final List<MyEventCalendar> unionList = new ArrayList<>();
        limitationAdapterFirebase.getLimitationByDateNoView(date, new LimitationAdapterFirebase.GetLimitationByDateListener() {
            @Override
            public void onComplete(List<LimitationEvent> limitationEventList) {

                unionList.addAll(limitationEventList);
                Log.d(TAG,"after add limitationList, size= " +limitationEventList.size()+" unionList size= "+unionList.size());
                unionList.addAll(appointmentList);
                Log.d(TAG,"after add appointmentList, size= " +appointmentList.size()+" unionList size= "+unionList.size());
                Comparator<MyEventCalendar> comparator = new Comparator<MyEventCalendar>() {
                    @Override
                    public int compare(MyEventCalendar left, MyEventCalendar right) {
                        Date leftTime = toTime(left.getEndHour());
                        Date rigthTime = toTime(right.getStartHour());
                        if (leftTime.compareTo(rigthTime) == 0)
                            return 0;
                        else if (leftTime.compareTo(rigthTime) > 0)
                            return 1;
                        else return -1;
                    }
                };
                Collections.sort(unionList, comparator);
                for (int i = 0; i < unionList.size(); i++) {
                    Log.d(TAG, "id:" + unionList.get(i).getStartHour() + " " + unionList.get(i).getEndHour());
                }
                listener.onComplete(unionList);
            }
        });
    }
    private void compactList(List<MyEventCalendar> unionList,final  String date) {
        int index = 0;
        int nextIndex = 0;
        while (nextIndex +1 < unionList.size()) {
            nextIndex++;
            Date eventEndhour = toTime(unionList.get(index).getEndHour());
            Date nextEvent = toTime(unionList.get(nextIndex).getStartHour());

            if (eventEndhour.compareTo(nextEvent) != 0) {
                if (unionList.get(nextIndex) instanceof LimitationEvent ||
                        (unionList.get(nextIndex) instanceof Appointment &&
                                ((Appointment) unionList.get(nextIndex)).getClientId() != null)) {
                    addEmptyAppointmenttoWindows(date, eventEndhour, nextEvent);
                } else {

                    Date endNextEvent = toTime((unionList.get(nextIndex).getEndHour()));
                    int minutesEnd = endNextEvent.getMinutes() + endNextEvent.getHours() * 60;
                    int minutesStart = nextEvent.getMinutes() + nextEvent.getHours() * 60;
                    int duration = minutesEnd - minutesStart;

                    Log.d(TAG,"set nextIndex = "+ unionList.get(nextIndex));
                    unionList.get(nextIndex).setStartHour(unionList.get(index).getEndHour());
                    Log.d(TAG,"new StartHour= "+ unionList.get(index).getEndHour());
                    Date end = addMinutesToTime(Long.toString(duration), eventEndhour);
                    String newEnd = mdf.format(end);
                    Log.d(TAG,"set endHour = "+ newEnd);

                    unionList.get(nextIndex).setEndHour(newEnd);
                    appointmentAdapterFirebase.addAppointment((Appointment)unionList.get(nextIndex), new AppointmentAdapterFirebase.GetAddAppointmentListener() {
                        @Override
                        public void onComplete(boolean onSucess) {
                            Log.d(TAG, "update window list");
                        }
                    });
                }
            }
            index++;
        }
        String lastHourEndAppointment = unionList.get(unionList.size()-1).getEndHour();
        Log.d(TAG,"the hour to last appointment is: "+ lastHourEndAppointment);
        final Date lastHour = toTime(lastHourEndAppointment);

        workDayAdapterFirebase.getWorkDayByDate(date, new WorkDayAdapterFirebase.GetWorkDayByDateListener() {
            @Override
            public void onComplete(WorkDay workDay) {
                String sClusehour=workDay.getCloseHour();
                Date clouseHour = toTime(sClusehour);
                Log.d(TAG,"the clouse hour is: " + sClusehour);
                if (lastHour.compareTo(clouseHour) < 0) {
                    Log.d(TAG,"the latest end hour from appointment is before clouse hour, check to add new empty appointment");

                    addEmptyAppointmenttoWindows(date, lastHour, clouseHour);
                }
            }
        });
    }
    private void addEmptyAppointmenttoWindows(final String date, final Date startWindow, final Date endWindow) {
        Log.d(TAG, "in addEmptyAppointmenttoWindows() ");
        treatmentsAdapterFirebase.getMaxDurationTreatment(new TreatmentsAdapterFirebase.GetMaxDurationTreatmentListener() {
            @Override
            public void onComplete(Treatments maxDurationTreatment) {
                mEndAppointmnet = addMinutesToTime(maxDurationTreatment.getDuration(), startWindow);
                if (!(mEndAppointmnet.compareTo(endWindow) > 0)) {
                    addNewAppointment(date, startWindow, mEndAppointmnet, maxDurationTreatment.getId());
                    Log.d(TAG,"the empty windows allowing to add the max duration treatment");
                }
                else {
                    treatmentsAdapterFirebase.getTreatmentsList(new TreatmentsAdapterFirebase.GetTreatmentListListener() {
                        @Override
                        public void onComplete(List<Treatments> treatmentsList) {
                            int index = treatmentsList.size() - 1;
                            while (index > -1) {
                                String duration = treatmentsList.get(index).getDuration();
                                mEndAppointmnet = addMinutesToTime(duration, startWindow);
                                if (!(mEndAppointmnet.compareTo(endWindow) > 0)) {
                                    Log.d(TAG,"the empty windows allowing to add new appointment with less duration ");
                                    addNewAppointment(date, startWindow, mEndAppointmnet, treatmentsList.get(index).getId());
                                    break;
                                }
                                index--;
                            }
                        }
                    });
                }
                if(!(mEndAppointmnet.compareTo(endWindow)>0))
                {
                    Log.d(TAG,"recursion to check if the empty windows allowing to add treatment");
                    addEmptyAppointmenttoWindows(date,mEndAppointmnet,endWindow);
                }
            }
        });
    }
    private Date addMinutesToTime(String minutes, Date time) {
        Date date = new Date();
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(time);
        long lminutes = Long.parseLong(minutes);
        long hours = lminutes / 60;
        long minnutesRemaining = lminutes % 60;
        newCalendar.add(Calendar.MINUTE, (int) minnutesRemaining);
        newCalendar.add(Calendar.HOUR, (int) hours);
        date = newCalendar.getTime();
        Date test = toTime(mdf.format(date));
        return test;
    }
    private void addNewAppointment(String date,
                                   Date startHourAppointment, Date endAppointment, String treatmentId) {
        Log.d(TAG, "in addNewAppointment() ");
        final String appointmentId = appointmentAdapterFirebase.getNewAppointmentId();
        String sStartAppointemet = mdf.format(startHourAppointment);
        String sEndAppointment = mdf.format(endAppointment);
        final Appointment newAppointment = new Appointment(appointmentId, date, sStartAppointemet, sEndAppointment,
                treatmentId, null);
        Log.d(TAG, "new Empty appointemet to add: appointmentId= " + appointmentId + "  date= " + date +
                "  startHourAppointment= " + startHourAppointment + "  endAppointment= " + endAppointment +
                "  treatmentId= " + treatmentId);
        appointmentAdapterFirebase.addAppointment(newAppointment,
                new AppointmentAdapterFirebase.GetAddAppointmentListener() {
                    @Override
                    public void onComplete(boolean onSucess) {
                        if (onSucess) {

                            Log.d(TAG, "empty appointment " + appointmentId + " added");
                        } else {
                            Log.d(TAG, "empty appointment " + appointmentId + " failed to added ");
                        }

                    }
                });
    }

    public interface GetDatesToAppointmentListener {
        void onComplete(List<Appointment> emptyAppointments);
    }
    public interface GetSchechuleListener{
        void onComplete(boolean isSuccess);
    }
    public interface GetVeridicationLimitation {
        void onComplete(boolean isVerificaded);
    }
    public interface GetUnionListListener{
        void onComplete(List<MyEventCalendar> list);
    }
}
