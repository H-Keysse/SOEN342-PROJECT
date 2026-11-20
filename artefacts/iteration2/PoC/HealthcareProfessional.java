/*The CSV provides the professional's name, title and specialty.
We keep this information directly in the class for availability searches instead of implementing a separate MedicalSpecialty
object. isExt also not needed here yet.*/


public class HealthcareProfessional {
    
    private String firstName;
    private String lastName;
    private String title;
    private String specialty;

    public HealthcareProfessional(String firstName, String lastName, String title, String specialty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.specialty = specialty;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getFullName() {
        return title+ " " + firstName + " " + lastName;
    }

  public boolean sameAs(String firstName, String lastName, String title, String specialty) {
    return this.firstName.equalsIgnoreCase(firstName)
        && this.lastName.equalsIgnoreCase(lastName)
        && this.title.equalsIgnoreCase(title)
        && this.specialty.equalsIgnoreCase(specialty);
  }
}
