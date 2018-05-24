import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Stream<String> data = null;
        HashMap<String, Integer> activityNr = new HashMap<>();
        HashMap<Integer, Integer> mapActivityDay = new HashMap<>();
        ArrayList<ActivityDay> activityDays = new ArrayList<>();
        HashMap<String, Long> durations = new HashMap<>();
        HashMap<String, Integer> filter = new HashMap<>();
        ArrayList<String> toRemove = new ArrayList<>();
        ArrayList<String> write = new ArrayList<>();
        ArrayList<MonitoredData> monitoredData = new ArrayList<>();

        int days, day;

        try {
            data = Files.lines(Paths.get("Activities.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> s = new ArrayList<String>();
        data.forEach(w -> {
            String[] token = w.split("\t\t");
            s.add(w);
            while(token[2].endsWith("\t")){
                token[2] = token[2].substring(0, token[2].length()-1);
            }
            MonitoredData aux = new MonitoredData(token[0], token[1], token[2]);
            monitoredData.add(aux);
            durations.putIfAbsent(token[2], (long)(0));
            durations.put(token[2], durations.get(token[2]) + getDurationSecods(aux.startTime, aux.endTime));
            if(getDurationSecods(aux.startTime, aux.endTime) < 300){
                filter.putIfAbsent(token[2], 0);
                filter.put(token[2], filter.get(token[2]) + 1);
            }
        });

        days = setDay(monitoredData.get(0).startTime, monitoredData.get(monitoredData.size() - 1).endTime);
        //System.out.println("Days passed: " + days);
        write.add("Days passed: " + days);

        for(MonitoredData m : monitoredData){
            activityNr.putIfAbsent(m.activity, 0);
            activityNr.put(m.activity, activityNr.get(m.activity) + 1);
        }

        for(String m : activityNr.keySet()) {
            //System.out.println(m + "\t\t- " + activityNr.get(m));
            write.add(m + "\t\t- " + activityNr.get(m));
            if (filter.containsKey(m)){
                if (filter.get(m) > activityNr.get(m) * 90 / 100) {
                    toRemove.add(m);
                    //System.out.println("removing activity: " + m);
                    write.add("removing activity: " + m);
                }
            }
        }

        for(String a : toRemove){
            ArrayList<MonitoredData> aux = new ArrayList<>();
            for(MonitoredData m : monitoredData){
                if(m.activity.equals(a)){
                    aux.add(m);
                }
            }
            for(MonitoredData m : aux){
                monitoredData.remove(m);
            }
        }

        for(MonitoredData m : monitoredData){
            day = setDay(monitoredData.get(0).startTime, m.endTime);
            ActivityDay ad = new ActivityDay(day, m.activity);
            activityDays.add(ad);
            mapActivityDay.putIfAbsent(activityDays.indexOf(ad), 0);
            mapActivityDay.put(activityDays.indexOf(ad), mapActivityDay.get(activityDays.indexOf(ad)) + 1);
            //System.out.println(m + "\t\t- duration: " + getDuration(m.startTime, m.endTime));
            write.add(m + "\t\t- duration: " + getDuration(m.startTime, m.endTime));
        }

        for(Integer i : mapActivityDay.keySet()){
            //System.out.println("day: " + activityDays.get(i).day + " - Activity: " + activityDays.get(i).activity + "\t\t- " + mapActivityDay.get(i) + " times");
            write.add("day: " + activityDays.get(i).day + " - Activity: " + activityDays.get(i).activity + "\t\t- " + mapActivityDay.get(i) + " times");
        }

        for(String a : durations.keySet()){
            //System.out.println("Activity: " + a + "\t duration - " + (int)(durations.get(a)/3600) + ":" + (int)(durations.get(a)%3600/60) + ":" + (int)(durations.get(a)%3600%60));
            write.add("Activity: " + a + "\t duration - " + (int)(durations.get(a)/3600) + ":" + (int)(durations.get(a)%3600/60) + ":" + (int)(durations.get(a)%3600%60));
        }

        try {
            Files.write(Paths.get("Output.txt"), write);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int setDay(LocalDateTime startTime, LocalDateTime endTime){
        int day;
        day = (endTime.getYear() - startTime.getYear()) * 365;
        day += endTime.getDayOfYear() - startTime.getDayOfYear();
        return day;
    }

    private static String getDuration(LocalDateTime startTime, LocalDateTime endTime){
        StringBuilder duration = new StringBuilder();
        duration.append(ChronoUnit.HOURS.between(startTime, endTime));
        duration.append(":");
        duration.append(ChronoUnit.MINUTES.between(startTime, endTime)%60);
        duration.append(":");
        duration.append(ChronoUnit.SECONDS.between(startTime, endTime)%60);
        return duration.toString();
    }

    private static long getDurationSecods(LocalDateTime startTime, LocalDateTime endTime){
        long sec;
        sec = ChronoUnit.SECONDS.between(startTime, endTime);
        return sec;
    }
}
