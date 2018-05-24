public class ActivityDay {
    int day;
    String activity;

    public ActivityDay(int day, String activity){
        this.day = day;
        this.activity = activity;
    }

    @Override
    public int hashCode() {
        int hash;
        hash = day + activity.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ActivityDay){
            if(this.day == ((ActivityDay) obj).day){
                if(this.activity.equals(((ActivityDay) obj).activity)){
                    return true;
                }
            }
        }
        return false;
    }
}
