import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
public class CountryWithoutProvinces extends Country{
    private Map<LocalDate, DailyStatistic> dailyStatistics = new HashMap<>();
    public CountryWithoutProvinces(String name) {
        super(name);
    }
    public void addDailyStatistic(LocalDate date, int infections, int deaths) {
        dailyStatistics.put(date, new DailyStatistic(infections, deaths));
    }
    private static class DailyStatistic {
        int infections;
        int deaths;
        public DailyStatistic(int infections, int deaths) {
            this.infections = infections;
            this.deaths = deaths;
        }
    }
    @Override
    public int getConfCases(LocalDate date){
        return dailyStatistics
                .get(date)
                .infections;
    }
    @Override
    public int getDeaths(LocalDate date){
        return dailyStatistics
                .get(date)
                .deaths;
    }
}