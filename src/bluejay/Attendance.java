package bluejay;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Attendance implements Comparable<Attendance> {
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private LocalTime timecardHours;

    public Attendance() {
    }

    public Attendance(LocalDateTime timeIn, LocalDateTime timeOut, LocalTime timecardHours) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.timecardHours = timecardHours;
    }


    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

    public LocalTime getTimecardHours() {
        return timecardHours;
    }

    public void setTimecardHours(LocalTime timecardHours) {
        this.timecardHours = timecardHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(getTimeIn(), that.getTimeIn()) && Objects.equals(getTimeOut(), that.getTimeOut());
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "timeIn=" + timeIn +
                ", timeOut=" + timeOut +
                ", timecardHours=" + timecardHours +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimeIn(), getTimeOut());
    }

    @Override
    public int compareTo(Attendance o) {
        if (this.timeIn == null) return -1;
        if (this.timeOut == null) return 1;
        return this.timeIn.compareTo(o.timeIn);
    }
}


