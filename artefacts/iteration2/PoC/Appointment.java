/*
The full UML Appointment class includes arrivalTime, priority, requestedFrom and requestedTo for other parts of the system.
They are omitted because it's only the PoC.
The appointment's date, time, professional and clinic are already stored through Schedule so they aren't added here.
*/
 
 

public class Appointment {
    private String status;
    private MedicalService service;
    private Patient patient;
    private Schedule schedule;

    public Appointment(Patient patient, Schedule schedule, MedicalService service) {
        this.patient=patient;
        this.schedule=schedule;
        this.service = service;
        this.status= "REQUESTED";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public MedicalService getService() {
        return service;
    }

    public Patient getPatient() {
        return patient;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
