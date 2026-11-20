import java.util.ArrayList;

public class AppointmentDB {
    private ArrayList<Appointment> appointments = new ArrayList<>();

  public ArrayList<Appointment> getAppointmentList() {
    return appointments;
  }

    public void addAppointment(Appointment a) {
        appointments.add( a );
    }

    public void removeAppointment(Appointment a) {
        appointments.remove(a);
    }
}
