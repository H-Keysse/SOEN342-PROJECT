/*For the PoC, MedicalService only stores the service name.*/


public class MedicalService {
    private String name;

    public MedicalService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}