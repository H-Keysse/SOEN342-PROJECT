import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class BookingController {
    private PatientDB patientDB;
    private ScheduleDB scheduleDB;
    private AppointmentDB appointmentDB;

    
    private Patient selectedPatient;
    private MedicalService selectedService;
    private Appointment currentAppointment;

    public BookingController(PatientDB patientDB, ScheduleDB scheduleDB, AppointmentDB appointmentDB) {
        this.patientDB = patientDB;
        this.scheduleDB = scheduleDB;
        this.appointmentDB = appointmentDB;
    }

    
    public ArrayList<Patient> searchPatient(String searchCriteria) {
        return patientDB.searchPatient(searchCriteria);
    }

    // The UML class diagram uses patientID and hID, but the PoC passes them as objects


    public ArrayList<Schedule> selectPatientAndService(Patient patient, String medicalService, String specialty) {
        selectedPatient = patient;
        selectedService = new MedicalService(medicalService);
        return scheduleDB.findAvailableSlots(2, specialty);
    }

    
    public Appointment selectAppointment(HealthcareProfessional professional, LocalDate date, LocalTime time) {
        Schedule s = scheduleDB.findSchedule(professional, date, time);
        if(s==null) {
            return null;
        }
        Appointment a = new Appointment(selectedPatient, s, selectedService);
        appointmentDB.addAppointment(a);
        s.reserve(date, time);
        currentAppointment = a;
        return a;
    }


    
    public Appointment confirmAppointment() {
        currentAppointment.setStatus("CONFIRMED");
        Appointment a = currentAppointment;
        currentAppointment = null;
        selectedPatient = null;
        selectedService = null;

        return a;
    }

    // Only cancels unfinished booking when declined at confirmation and frees the slot

  public void cancelBooking() {
    Schedule s = currentAppointment.getSchedule();
    s.release(s.getAvailableFrom().toLocalDate(), s.getAvailableFrom().toLocalTime());
    appointmentDB.removeAppointment(currentAppointment);
    currentAppointment = null;
    selectedPatient = null;
    selectedService = null;
  }
}
