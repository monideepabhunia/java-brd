
package bluejay;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

public class Main {

    public static void main(String[] args) {
        String filePath = "data.csv";

        List<String> result = solve(getEmployeesFromFile(filePath));

        StringBuilder sb = new StringBuilder();

        sb.append("Employees who have worked for 7 consecutive days :\n").append(result.get(0)).append("\n\n");
        sb.append("Employees who have less than 10hrs but greater than 1hr between shifts :\n").append(result.get(1)).append("\n\n");
        sb.append("Employees who have worked for more than 14hrs in a shift :\n").append(result.get(2));

        System.out.println(sb);
        writeFile("output.txt", sb.toString());
    }

    public static Set<Employee> getEmployeesFromFile(String filePath) {

        String fileContents = readFile(filePath);

        String lines[] = fileContents.split("\n");
        List<Employee> employees = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            try {
                String words[] = lines[i].substring(0, lines[i].length() - 3).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String positionId = words[0];
                String positionStatus = words[1];
                String timeIn = words[2];
                String timeOut = words[3];
                String timecardHours = words[4];
                String payCycleStartDate = words[5];
                String payCycleEndDate = words[6];
                String empName = words[7];
                String empFileNo = words[8];


                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a").withLocale(Locale.US);
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("H:mm").withLocale(Locale.US);
                DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("MM/dd/yyyy").withLocale(Locale.US);

                LocalDateTime actualTimeIn = timeIn.length() > 1 ? LocalDateTime.parse(timeIn, formatter1) : null;
                LocalDateTime actualTimeOut = timeOut.length() > 1 ? LocalDateTime.parse(timeOut, formatter1) : null;
                LocalTime actualTimecardHours = timecardHours.length() > 1 ? LocalTime.parse(timecardHours, formatter2) : null;

                LocalDate actualPayCycleStartDate = payCycleStartDate.length() > 1 ? LocalDate.parse(payCycleStartDate, formatter3) : null;
                LocalDate actualPayCycleEndDate = payCycleEndDate.length() > 1 ? LocalDate.parse(payCycleEndDate, formatter3) : null;
                Integer actualEmpFileNo = empFileNo.length() > 0 ? Integer.parseInt(empFileNo) : null;
                Employee emp = new Employee(positionId, positionStatus, actualPayCycleStartDate, actualPayCycleEndDate, empName, actualEmpFileNo);
                Attendance record = null;
                if (actualTimecardHours != null || (actualTimeIn != null && actualTimeOut != null)) {
                    record = new Attendance(actualTimeIn, actualTimeOut, actualTimecardHours);
                }

                int idx = employees.indexOf(emp);
                if (idx != -1) {
                    Employee e = employees.get(idx);
                    if (record != null) e.getAttendanceRecords().add(record);
                } else {
                    employees.add(emp);
                    if (record != null) emp.getAttendanceRecords().add(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new HashSet<>(employees);
    }

    /**
     * @param filePath Path of the input file as String
     * @return All contents of the file as a single String
     * @see After analysing the data, assuming each line having 150 characters on average,
     * this program can read INTEGER.MAX_VALUE / 150 = 1,43,16,557 lines of data without any hassle.
     * This limit (1,43,16,557) is far greater than the max permitted row length in an excel sheet(10,48,576)
     */
    public static String readFile(String filePath) {
        String fContents = null;
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fContents = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fContents;
    }

    public static List<String> solve(Set<Employee> employees) {
        StringBuilder case1 = new StringBuilder();
        StringBuilder case2 = new StringBuilder();
        StringBuilder case3 = new StringBuilder();

        for (Employee emp : employees) {
            try {
                List<Attendance> records = new ArrayList<>(emp.getAttendanceRecords());

                //  Checking for Case 1 : Who have worked for 7 consecutive days
                List<LocalDate> dates = emp.getAttendaceDates();
                int counter = 1;
                int max = 0;
                for (int i = 0; i < dates.size() - 1; i++) {
                    if (DAYS.between(dates.get(i), dates.get(i + 1)) > 1) {
                        max = Math.max(counter, max);
                        counter = 1;
                    } else counter++;
                }

                boolean hasWorkedfor7days = max >= 7;
                if (hasWorkedfor7days) case1.append(emp).append("\n");


                //  Checking for Case 2 :  Who have less than 10 hours of time between shifts but greater than 1 hr
                boolean hasWorkedBetween1and10hours = false;
                for (int i = 0; i < records.size() - 1; i++) {
                    if (DAYS.between(records.get(i).getTimeIn(), records.get(i + 1).getTimeIn()) == 0) {
                        long hours = HOURS.between(records.get(i).getTimeOut(), records.get(i + 1).getTimeIn());
                        hasWorkedBetween1and10hours = hours > 1 && hours < 10;
                        if (hasWorkedBetween1and10hours) break;
                    }
                }
                if (hasWorkedBetween1and10hours) case2.append(emp).append("\n");


                // Checking for Case 3 : Who has worked for more than 14 hours in a single shift
                boolean hasWorkedFor14hrs = false;
                for (Attendance record : records) {
                    long hours = record.getTimecardHours().getHour();
                    hasWorkedFor14hrs = hours > 14;
                    if (hasWorkedFor14hrs) break;
                }

                if (hasWorkedFor14hrs) case3.append(emp).append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<String> result = new ArrayList<>();
        result.add(case1.toString());
        result.add(case2.toString());
        result.add(case3.toString());
        return result;
    }


    public static void writeFile(String filePath, String data){
        try(FileOutputStream output  = new FileOutputStream(filePath)){
            byte[] arr = data.getBytes();
            output.write(arr);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
