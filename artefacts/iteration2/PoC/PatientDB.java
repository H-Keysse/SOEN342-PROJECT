import java.util.ArrayList;

public class PatientDB {
    private ArrayList<Patient> patients = new ArrayList<>();

  public ArrayList<Patient> searchPatient(String searchCriteria) {
    ArrayList<Patient> list = new ArrayList<>();
    for(Patient p : patients) {
      if(p.getName().toLowerCase().contains(searchCriteria.toLowerCase())) {
        list.add(p);
      }
    }
    return list;
  }

    public void addPatient(Patient p) {
        patients.add(p);
    }
}
