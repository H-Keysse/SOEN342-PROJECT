/*Each Schedule represents availability slot for a professional at a clinic. 
LocalDateTime is used instead of availableFrom and availableTo attributes from our UML. 
Booked attribute indicates slot occupation.*/



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Schedule {
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private boolean booked;
    private HealthcareProfessional professional;
    private ClinicalLocation location;

    public Schedule(HealthcareProfessional professional, LocalDateTime availableFrom, LocalDateTime availableTo,
                    ClinicalLocation location) {
        this.professional = professional;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.location = location;
        this.booked = false;
    }

    public boolean isAvailable(LocalDate date, LocalTime time) {
        return !booked && availableFrom.toLocalDate().equals(date)
                && availableFrom.toLocalTime().equals(time);
    }

  public void reserve(LocalDate date, LocalTime time) {
    if(isAvailable(date,time)) {
      booked = true;
    }
  }

    public void release(LocalDate date, LocalTime time) {
        if (availableFrom.toLocalDate().equals(date) && availableFrom.toLocalTime().equals(time)) {
            booked = false;
        }
    }


    public boolean isBooked() {
        return booked;
    }

    public LocalDateTime getAvailableFrom() {
        return availableFrom;
    }

    public LocalDateTime getAvailableTo() {
        return availableTo;
    }

    public HealthcareProfessional getProfessional() {
        return professional;
    }

    public ClinicalLocation getLocation() {
        return location;
    }

    public String describe() {
        return professional.getFullName()+" ("+professional.getSpecialty()+") | "
                + availableFrom.toLocalDate() + " " + availableFrom.toLocalTime() + "-"
                + availableTo.toLocalTime() + " | " + location.getClinicName();
    }
}
