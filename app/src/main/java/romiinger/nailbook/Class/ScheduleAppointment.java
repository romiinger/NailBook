package romiinger.nailbook.Class;

import android.app.usage.UsageEvents;
import android.util.Log;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import romiinger.nailbook.Firebase.AppointmentAdapterFirebase;
import romiinger.nailbook.Firebase.LimitationAdapterFirebase;
import romiinger.nailbook.Firebase.TreatmentsAdapterFirebase;
import romiinger.nailbook.Firebase.WalletAdapterFirebase;
import romiinger.nailbook.Firebase.WorkDayAdapterFirebase;

public class ScheduleAppointment {
    private static final String TAG = "AppointmentAdapterF";
    private Calendar cal = Calendar.getInstance();
    private DateFormat mdf = new SimpleDateFormat("HH:mm");
    private WorkDayAdapterFirebase workDayAdapterFirebase;
    private AppointmentAdapterFirebase appointmentAdapterFirebase;
    private TreatmentsAdapterFirebase treatmentsAdapterFirebase;
    private WalletAdapterFirebase walletAdapterFirebase;
    private LimitationAdapterFirebase limitationAdapterFirebase;
    private WorkDay mWorkday;
    private List<Appointment> mAppointmentList;
    private Treatments mTreatment;

    public ScheduleAppointment() {
        workDayAdapterFirebase = new WorkDayAdapterFirebase();
        appointmentAdapterFirebase = new AppointmentAdapterFirebase();
        treatmentsAdapterFirebase = new TreatmentsAdapterFirebase();
        walletAdapterFirebase = new WalletAdapterFirebase();
        limitationAdapterFirebase = new LimitationAdapterFirebase();
    }


    public interface GetDatesToAppointmentListener {
        void onComplete(List<Appointment> emptyAppointments);
    }

    public void getEmptyAppointments(final String date, final GetDatesToAppointmentListener listener) {
        treatmentsAdapterFirebase.getMaxDurationTreatment(
                new TreatmentsAdapterFirebase.GetMaxDurationTreatmentListener() {
                    @Override
                    public void onComplete(final Treatments maxDurationTreatment) {
                        workDayAdapterFirebase.getWorkDayByDate(date,
                                new WorkDayAdapterFirebase.GetWorkDayByDateListener() {
                                    @Override
                                    public void onComplete(WorkDay workDay) {
                                        if (workDay.getDate().isEmpty() || workDay.getDate() == null) {
                                            Log.d(TAG, "no workday exist in this date");
                                            listener.onComplete(null);
                                        } else {
                                            mWorkday = workDay;
                                            Date openHour = toTime(mWorkday.getOpenHour());
                                            Date clouseHour = toTime(mWorkday.getCloseHour());
                                            if (openHour == null || clouseHour == null) {
                                                Log.d(TAG, "failed to cast string date to date");
                                                listener.onComplete(null);
                                            }
                                            final List<Appointment> emptyAppointments = new ArrayList<>();
                                            Date hourAppointment = openHour;
                                            cal.setTime(hourAppointment);
                                            Date endAppointment = null;
                                            do {
                                                String sHourAppointment = mdf.format(hourAppointment.getTime());
                                                addNewAppointment( date, sHourAppointment, maxDurationTreatment);
                                                cal.add(Calendar.MINUTE, Integer.parseInt(maxDurationTreatment.getDuration()));
                                                hourAppointment = cal.getTime();
                                                cal.add(Calendar.MINUTE, Integer.parseInt(maxDurationTreatment.getDuration()));
                                                endAppointment = cal.getTime();
                                            }
                                            while (endAppointment.compareTo(clouseHour) > 0);
                                            listener.onComplete(emptyAppointments);
                                        }
                                    }
                                });
                    }
                });
    }

    private void addNewAppointment(String date,
                                   String startHourAppointment, Treatments maxDurationTreatment) {
        final String appointmentId = appointmentAdapterFirebase.getNewAppointmentId();
        Date hour = toTime(startHourAppointment);
        cal.setTime(hour);
        cal.add(Calendar.MINUTE, Integer.parseInt(maxDurationTreatment.getDuration()));
        String endAppointment = mdf.format(cal.getTime());
        final Appointment newAppointment = new Appointment(appointmentId, date, startHourAppointment, endAppointment,
                maxDurationTreatment.getId(), null);
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

    public interface GetVeridicationLimitation {
        void onComplete(boolean isVerificaded);
    }

    public void veridicationAppointments(final LimitationEvent limitationEvent,
                                         final GetVeridicationLimitation listener) {

        //toDo limitation to add limitation if exist appointment client in the same hours
        appointmentAdapterFirebase.getClientAppointmnetByDate(limitationEvent.getDate(),
                new AppointmentAdapterFirebase.GetAppointmentsListListener() {
                    @Override
                    public void onComplete(List<Appointment> appointmentList) {
                        boolean verificator = false;
                        for (int i = 0; i < appointmentList.size(); i++) {
                            if (isOverlap(limitationEvent, appointmentList.get(i)))
                                listener.onComplete(false);
                        }
                        updateEmptyAppointments(limitationEvent, listener);
                    }
                });
        //toDo update empty appointments in firebae
    }

    private boolean isOverlap(final LimitationEvent limitationEvent, final Appointment appointment) {
        final boolean[] toReturn = new boolean[1];
        final Date startLimtationTime = toTime(limitationEvent.getStartHour());
        final Date endLimationTime = toTime(limitationEvent.getEndHour());
        if (startLimtationTime == null || endLimationTime == null) {
            Log.e(TAG, "Errror to parse string to time date in toTime");
            return true;
        }
        final Date startAppointment = toTime(appointment.getStartHour());
        treatmentsAdapterFirebase.getTreatmentById(appointment.getTreatmentId(),
                new TreatmentsAdapterFirebase.GetTreatmentByIdListener() {
                    @Override
                    public void onComplete(Treatments treatment) {
                        cal.setTime(startAppointment);
                        cal.add(Calendar.MINUTE, Integer.parseInt(treatment.getDuration()));
                        Date endAppointment = cal.getTime();
                        if (startLimtationTime.compareTo(startAppointment) == 0 ||
                                (startLimtationTime.compareTo(startAppointment) < 0 &&
                                        !(startAppointment.compareTo(endLimationTime) > 0)) ||
                                (startAppointment.compareTo(startLimtationTime)) < 0 &&
                                        (startLimtationTime.compareTo(endAppointment) < 0)) {
                            toReturn[0] = true;
                        }
                    }
                });
        if (toReturn[0])
            return true;
        else return false;
    }

    public void updateEmptyAppointments(final LimitationEvent limitationEvent, final GetVeridicationLimitation listener) {
        //remove emptys appointmenys overlap to limitaion event
        appointmentAdapterFirebase.getEmptyAppointmentsByDate(limitationEvent.getDate(),
                new AppointmentAdapterFirebase.GetAppointmentsListListener() {
                    @Override
                    public void onComplete(final List<Appointment> appointmentList) {
                        for (int i = 0; i < appointmentList.size(); i++) {
                            if (isOverlap(limitationEvent, appointmentList.get(i))) {
                                appointmentAdapterFirebase.removeApoointmentById(appointmentList.get(i));
                                appointmentList.remove(i);
                            }
                        }
                        //update emptys appointment lis
                        limitationAdapterFirebase.getLimitationByDate(limitationEvent.getDate(), new LimitationAdapterFirebase.GetLimitationByDateListener() {
                            @Override
                            public void onComplete(List<LimitationEvent> limitationEventList) {
                                List<MyEventCalendar> unionList = new ArrayList<>();
                                unionList.addAll(limitationEventList);
                                unionList.addAll(appointmentList);
                                Collections.sort(unionList);
                                //List<Object> unionList = joinList(limitationEventList,appointmentList);
                                int index = 0;
                                while (index < unionList.size()) {
                                    Date event = toTime(unionList.get(index).getEndHour());
                                    Date nextEvent = toTime(unionList.get(index++).getStartHour());

                                    if (event.compareTo(nextEvent) != 0) {
                                        if (unionList.get(index++) instanceof LimitationEvent ||
                                                (unionList.get(index++) instanceof Appointment &&
                                                        ((Appointment) unionList.get(index++)).getClientId() != null)) {
                                            addEmptyAppointmenttoWindows(limitationEvent.getDate(), event, nextEvent);
                                        } else {

                                            Date endNextEvent = toTime((unionList.get(index++).getEndHour()));
                                            long duration = endNextEvent.getTime() - nextEvent.getTime();
                                            Appointment appointment = (Appointment) unionList.get(index++);
                                            appointment.setStartHour(mdf.format(event));
                                            cal.setTime(event);
                                            cal.add(Calendar.MINUTE, (int) duration);
                                            String newEnd = mdf.format(cal.getTime());
                                            appointment.setEndHour(newEnd);
                                            appointmentAdapterFirebase.addAppointment(appointment, new AppointmentAdapterFirebase.GetAddAppointmentListener() {
                                                @Override
                                                public void onComplete(boolean onSucess) {
                                                    Log.d(TAG, "update window list");
                                                }
                                            });
                                        }
                                    }

                                }
                                listener.onComplete(true);
                            }
                        });
                    }
                });
    }

    private void addEmptyAppointmenttoWindows(final String date, final Date startWindow, final Date endWindow) {
        treatmentsAdapterFirebase.getMaxDurationTreatment(new TreatmentsAdapterFirebase.GetMaxDurationTreatmentListener() {
            @Override
            public void onComplete(Treatments maxDurationTreatment) {
                cal.setTime(startWindow);
                cal.add(Calendar.MINUTE, Integer.parseInt(maxDurationTreatment.getDuration()));
                if (!(cal.getTime().compareTo(endWindow) > 0))
                    addNewAppointment(date, mdf.format(startWindow), maxDurationTreatment);
                else {
                    treatmentsAdapterFirebase.getTreatmentsList(new TreatmentsAdapterFirebase.GetTreatmentListListener() {
                        @Override
                        public void onComplete(List<Treatments> treatmentsList) {
                            int index = treatmentsList.size() - 1;
                            while (index > 0) {
                                cal.setTime(startWindow);
                                cal.add(Calendar.MINUTE, Integer.parseInt(treatmentsList.get(index).getDuration()));
                                if (!(cal.getTime().compareTo(endWindow) > 0)) {
                                    addNewAppointment(date, mdf.format(startWindow), treatmentsList.get(index));
                                    break;
                                }
                                index--;
                            }
                        }
                    });
                }
            }
        });
    }

    private Date toTime(String stringTime) {
        Date date = new Date();
        try {
            date = new Time(mdf.parse(stringTime).getTime());
            return date;
        } catch (Exception e) {
            Log.e(TAG, "Exception" + e);
            return null;
        }
    }

    private List<Object> joinList(List<LimitationEvent> limitationEventList, List<Appointment> appointmentList) {
        final List<Object> unionList = new ArrayList<>();
        int indexEventList = 0;
        int indexAList = 0;
        while (indexEventList < limitationEventList.size() && indexAList < appointmentList.size()) {
            Date eventD = toTime(limitationEventList.get(indexEventList).getDate());
            Date appointmentD = toTime(appointmentList.get(indexAList).getDate());
            if (eventD.compareTo(appointmentD) < 0) {
                unionList.add(limitationEventList.get(indexEventList));
                indexEventList++;
            } else {
                unionList.add(appointmentList.get(indexAList));
                indexAList++;
            }
        }
        if (indexEventList == limitationEventList.size() && indexAList < appointmentList.size()) {
            while (indexAList < appointmentList.size()) {
                unionList.add(appointmentList.get(indexAList));
                indexAList++;
            }
        }
        if (indexEventList < limitationEventList.size() && indexAList == appointmentList.size()) {
            while (indexEventList < limitationEventList.size()) {
                unionList.add(limitationEventList.get(indexEventList));
                indexEventList++;
            }
        }
        return unionList;
    }
}
