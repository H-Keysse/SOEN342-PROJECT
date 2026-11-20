import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static PatientDB patientDB = new PatientDB();
    private static ScheduleDB scheduleDB = new ScheduleDB();
    private static AppointmentDB appointmentDB = new AppointmentDB();
    private static BookingController bookingController = new BookingController(patientDB, scheduleDB, appointmentDB);

/* Console interface for the five required PoC features.
It collects user input and calls BookingController or ScheduleDB.
*/
    public static void main(String[] args) {
        boolean session = true;
        while(session) {
            System.out.println();
            System.out.println(" Clinic Appointment System  ");
            System.out.println("---------------------------------");
            System.out.println("1. Book Appointment");
            System.out.println("2. Search Availability");
            System.out.println("3. Make Availability");
            System.out.println("4. Import Availability from CSV");
            System.out.println("5. Export Availability to CSV");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String c= scanner.nextLine().trim();

            try {
                switch (c) {
                    case "1":
                        bookAppointment();
                        break;
                    case "2":
                        searchAvailability();
                        break;
                    case "3":
                        makeAvailability();
                        break;
                    case "4":
                        importCsv();
                        break;
                    case "5":
                        exportCsv();
                        break;
                    case "0":
                        session = false;
                        break;
                    default:
                        System.out.println("Please choose 0-5.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
        System.out.println("Session ending...");
    }

  private static void printSchedules(ArrayList<Schedule> list) {
    if (list.isEmpty()) {
      System.out.println("No available slots.");
      return;
    }
    for(int i = 0; i < list.size(); i++) {
      System.out.println((i + 1) + ". " + list.get(i).describe());
    }
  }

  // Book Appointment  

    private static void bookAppointment() {
        System.out.print("Search patient by name: ");
        String q = scanner.nextLine().trim();
        ArrayList<Patient> found = bookingController.searchPatient(q);
        for(int i=0;i<found.size();i++) {
            System.out.println((i + 1) + ". " + found.get(i).describe());
        }
        if(found.isEmpty()) {
            System.out.println("No matching patients.");
        }
        System.out.print("Choose patient number, or 0 to enter a new patient: ");
        String pick = scanner.nextLine().trim();
        Patient patient;
        if (pick.equals("0")) {
            System.out.print("Patient name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Phone: ");
            String phone = scanner.nextLine().trim();
            System.out.print("RAMQ number (optional): ");
            String ramq = scanner.nextLine().trim();
            patient = new Patient(name, phone, ramq);
            patientDB.addPatient(patient);
        } else {
            patient = found.get(Integer.parseInt(pick) - 1);
        }

        
        //Choose patient and service for the appointment

        System.out.print("Medical service: ");
        String service = scanner.nextLine().trim();
        System.out.print("Specialty (e.g. Cardiology): ");
        String specialty = scanner.nextLine().trim();
        ArrayList<Schedule> slots = bookingController.selectPatientAndService(patient, service, specialty);
        System.out.println("Available slots:");
        printSchedules(slots);
        if (slots.isEmpty()) {
            return;
        }

        //Pick the slot for the appointment if available, else return no availability

        System.out.print("Choose slot number: ");
        int n = Integer.parseInt(scanner.nextLine().trim());
        Schedule s = slots.get(n - 1);
        Appointment a = bookingController.selectAppointment(s.getProfessional(),
                s.getAvailableFrom().toLocalDate(), s.getAvailableFrom().toLocalTime());
        if (a == null) {
            System.out.println("That slot is no longer available.");
            return;
        }
        System.out.println("Appointment selected (status " + a.getStatus() + ").");

        //Confirm the booking or cancel it

        System.out.print("Confirm appointment? (y/n): ");
        String ans = scanner.nextLine().trim();
        if(ans.equalsIgnoreCase("y")) {
            Appointment done = bookingController.confirmAppointment();
            System.out.println("Appointment " + done.getStatus() + ": " + done.getPatient().getName()
            + " - " + done.getService().getName() + " - " + done.getSchedule().describe());
        } else {
            bookingController.cancelBooking();
            System.out.println("Booking cancelled. The slot is free again.");
        }
    }

    //Searches for available HealthcareProfessional using 3 filters
    
  private static void searchAvailability() {
    System.out.println("Filter: 0 = none, 1 = professional name, 2 = specialty, 3 = date (YYYY-MM-DD)");
    System.out.print("Choose filter: ");
    int filter = Integer.parseInt(scanner.nextLine().trim());
    String value = "";
    if(filter != 0) {
        if (filter == 1) {
          System.out.print("Professional name: ");
        } else if (filter == 2) {
          System.out.print("Specialty: ");
        } else {
          System.out.print("Date (YYYY-MM-DD): ");
        }
        value = scanner.nextLine().trim();
      }
    printSchedules(scheduleDB.findAvailableSlots(filter, value));
  }

  // Make availability with optional pattern (weekly monthy etc.)
   
    private static void makeAvailability() {
        ArrayList<HealthcareProfessional> list = scheduleDB.getProfessionals();
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i).getFullName() + " (" + list.get(i).getSpecialty() + ")");
        }
        if (list.isEmpty()) {
            System.out.println("No professionals yet. Import availability from CSV first.");
            return;
        }
        System.out.print("Choose professional number: ");
        String pick = scanner.nextLine().trim();
        HealthcareProfessional hp = list.get(Integer.parseInt(pick) - 1);

        System.out.print("Start date (YYYY-MM-DD): ");
        LocalDate from = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("End date (YYYY-MM-DD): ");
        LocalDate to = LocalDate.parse(scanner.nextLine().trim());
        System.out.println("Repetition: 1 = no repetition (start date only), 2 = weekly, 3 = monthly");
        System.out.print("Choose repetition: ");
        int pattern = Integer.parseInt(scanner.nextLine().trim());
        String weekdays = "";
        int dayOfMonth = 0;
        if(pattern == 2) {
            System.out.print("Weekday(s), e.g. MON,WED: ");
            weekdays = scanner.nextLine().trim();
        } else if(pattern == 3) {
            System.out.print("Day of month (1-31): ");
            dayOfMonth = Integer.parseInt(scanner.nextLine().trim());
        }
        System.out.print("Daily start time (HH:mm): ");
        LocalTime dayStart = LocalTime.parse(scanner.nextLine().trim());
        System.out.print("Daily end time (HH:mm): ");
        LocalTime dayEnd = LocalTime.parse(scanner.nextLine().trim());

        int n = scheduleDB.makeAvailability(hp, from, to, pattern, weekdays, dayOfMonth, dayStart, dayEnd);
        System.out.println(n + " slot(s) of 30 minutes created for " + hp.getFullName() + ".");
    }

    //CSV imports and exports
  private static void importCsv() {
    System.out.print("CSV file path: ");
    String file = scanner.nextLine().trim();
    try {
      int n = scheduleDB.importCsv(file);
      System.out.println(n + " slot(s) imported.");
    } catch (Exception e) {
      System.out.println("Could not read file: " + file);
    }
  }


    
    private static void exportCsv() {
        System.out.print("Output file path: ");
        String path = scanner.nextLine().trim();
        try {
            int count = scheduleDB.exportCsv(path);
            System.out.println(count + " slot(s) exported to " + path + ".");
        } catch (Exception e) {
            System.out.println("Could not write file: " + path);
        }
    }
}
