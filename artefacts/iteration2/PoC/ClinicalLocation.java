public class ClinicalLocation {
    private String clinicName;
    private String address;

    public ClinicalLocation(String clinicName, String address) {
        this.clinicName = clinicName;
        this.address = address;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getAddress() {
        return address;
    }
}
