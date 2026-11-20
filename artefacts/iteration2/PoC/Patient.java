/* Limited implementation of the UML patient class since the PoC doesn't require the full implementation
hence missing patientID */


public class Patient {
    
    private String name;
    private String phone;
    private String ramqNumber;

    public Patient(String name, String phone, String ramqNumber) {
        this.name = name;
        this.phone = phone;
        this.ramqNumber = ramqNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRamqNumber() {
        return ramqNumber;
    }

    public String describe() {
        return name + " | " + phone + " | RAMQ: " + (ramqNumber.isEmpty() ? "none" : ramqNumber);
    }
}
