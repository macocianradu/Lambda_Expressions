import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MonitoredData {
    LocalDateTime startTime;
    LocalDateTime endTime;
    String activity;

    public MonitoredData(String startTime, String endTime, String activity){
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.startTime = LocalDateTime.parse(startTime, formatter1);
        this.endTime = LocalDateTime.parse(endTime, formatter1);
        this.activity = activity;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(startTime.toString());
        s.append("\t\t");
        s.append(endTime.toString());
        s.append("\t\t");
        s.append(activity);
        return s.toString();
    }
}
