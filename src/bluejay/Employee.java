
package bluejay;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Employee implements Comparable<Employee> {
    private String name;
    private String positionId;
    private LocalDate payCycleStartDate;
    private LocalDate payCycleEndDate;
    private int fileNumber;
    private String positionStatus;
    private Set<Attendance> attendanceRecords;

    public Employee() {
        this.attendanceRecords = new TreeSet<>();
    }

    public Employee(String positionId, String positionStatus, LocalDate payCycleStartDate, LocalDate payCycleEndDate, String name, int fileNumber) {
        this();
        this.positionId = positionId;
        this.positionStatus = positionStatus;
        this.payCycleStartDate = payCycleStartDate;
        this.payCycleEndDate = payCycleEndDate;
        this.name = name;
        this.fileNumber = fileNumber;
    }

    public Employee(String name, String positionId, int fileNumber) {
        this();
        this.name = name;
        this.positionId = positionId;
        this.fileNumber = fileNumber;
    }

    public Set<Attendance> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(Set<Attendance> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name=" + name +
                ", positionId='" + positionId + '\'' +
                '}';
    }

    public List<LocalDate> getAttendaceDates() {
        return getAttendanceRecords().stream().map(x -> x.getTimeIn().toLocalDate()).distinct().collect(Collectors.toList());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return getFileNumber() == employee.getFileNumber()
                && Objects.equals(getName(), employee.getName())
                && Objects.equals(getPositionId(), employee.getPositionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPositionId(), getFileNumber());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public LocalDate getPayCycleStartDate() {
        return payCycleStartDate;
    }

    public void setPayCycleStartDate(LocalDate payCycleStartDate) {
        this.payCycleStartDate = payCycleStartDate;
    }

    public LocalDate getPayCycleEndDate() {
        return payCycleEndDate;
    }

    public void setPayCycleEndDate(LocalDate payCycleEndDate) {
        this.payCycleEndDate = payCycleEndDate;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }

    @Override
    public int compareTo(Employee o) {
        return 0;
    }
}

