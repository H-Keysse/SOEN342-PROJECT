import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class ScheduleDB {
    private ArrayList<Schedule> schedules = new ArrayList<>();

    
    private ClinicalLocation mainClinic = new ClinicalLocation("First Clinic", "Montreal, QC");

    
    // Class diagram shows separate availability search methods but the PoC has only one method handles different searches using the filter below.
    
    public ArrayList<Schedule> findAvailableSlots(int filter, String value) {
        ArrayList<Schedule> slots = new ArrayList<>();
        String v = value.toLowerCase();
        for (Schedule s : schedules) {
            if(s.isBooked()) {
                continue;
            }
            HealthcareProfessional hp = s.getProfessional();
            if (filter == 0
                    || (filter == 1 && (hp.getFirstName() + " " + hp.getLastName()).toLowerCase().contains(v))
                    || (filter == 2 && hp.getSpecialty().toLowerCase().contains(v))
                    || (filter == 3 && s.getAvailableFrom().toLocalDate().toString().equals(value))) {
                slots.add(s);
            }
        }
        return slots;
    }

    

    public Schedule findSchedule(HealthcareProfessional professional, LocalDate date, LocalTime time) {
        for (Schedule s : schedules) {
            if(s.getProfessional()==professional && s.isAvailable(date, time)) {
                return s;
            }
        }
        return null;
    }

    

  public ArrayList<HealthcareProfessional> getProfessionals() {
    ArrayList<HealthcareProfessional> list = new ArrayList<>();
    for (Schedule s : schedules) {
      if (!list.contains(s.getProfessional())) {
        list.add(s.getProfessional());
      }
    }
    return list;
  }
  // Avoids duplicates, reuses professional if it already exists
    public HealthcareProfessional findOrCreateProfessional(String firstName, String lastName, String title,
                                                           String specialty) {
        for (HealthcareProfessional hp : getProfessionals()) {
            if (hp.sameAs(firstName, lastName, title, specialty)) {
                return hp;
            }
        }
        return new HealthcareProfessional(firstName, lastName, title, specialty);
    }

    
    public int makeAvailability(HealthcareProfessional professional, LocalDate startDate, LocalDate endDate,
                                int pattern, String weekdays, int dayOfMonth,
                                LocalTime dayStart, LocalTime dayEnd) {
        int created = 0;
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            boolean ok = false;
            if (pattern == 1) {
                ok = date.equals(startDate);
            } else if (pattern == 2) {
                String[] days = weekdays.split(",");
                for(String d : days) {
                    if(date.getDayOfWeek().toString().startsWith(d.trim().toUpperCase())) {
                        ok = true;
                    }
                }
            } else if (pattern == 3) {
                ok = date.getDayOfMonth() == dayOfMonth;
            }

            if(ok) {
                
                LocalTime start = dayStart;
                while (!start.plusMinutes(30).isAfter(dayEnd)) {
                    schedules.add(new Schedule(professional, date.atTime(start),
                            date.atTime(start.plusMinutes(30)), mainClinic));
                    created++;
                    start = start.plusMinutes(30);
                }
            }
            date = date.plusDays(1);
        }
        return created;
    }

    

    public int importCsv(String path) throws IOException {
        int count = 0;
        BufferedReader in = new BufferedReader(new FileReader(path));
        String line = in.readLine(); // skip header
        while ((line = in.readLine()) != null) {
            String[] cols = line.split(",");
            HealthcareProfessional hp = findOrCreateProfessional(cols[0], cols[1], cols[2], cols[3]);
            LocalDate date = LocalDate.parse(cols[4]);
            LocalTime start = LocalTime.parse(cols[5]);
            LocalTime end = LocalTime.parse(cols[6]);
            schedules.add(new Schedule(hp, date.atTime(start), date.atTime(end), mainClinic));
            count++;
        }
        in.close();
        return count;
    }

    

    public int exportCsv(String path) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(path));
        out.write("First Name,Last Name,Title,Specialty,Date,Start,End");
        out.newLine();
        for(Schedule s : schedules) {
            HealthcareProfessional hp = s.getProfessional();
            out.write(hp.getFirstName() + "," + hp.getLastName() + "," + hp.getTitle() + ","
                    + hp.getSpecialty() + "," + s.getAvailableFrom().toLocalDate() + ","
                    + s.getAvailableFrom().toLocalTime() + "," + s.getAvailableTo().toLocalTime());
            out.newLine();
        }
        out.close();
        return schedules.size();
    }
}
