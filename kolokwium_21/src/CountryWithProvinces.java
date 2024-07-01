import java.awt.*;
import java.util.ArrayList;
import java.time.LocalDate;
public class CountryWithProvinces extends Country{
    private ArrayList<Country> provinces = new ArrayList<Country>();
    public CountryWithProvinces(String name, ArrayList<Country> provinces){
        super(name);
        this.provinces = provinces;
    }
    public CountryWithProvinces(String name, int provinceCount){
        super(name);
        for(int i = 0; i < provinceCount; i++){
            provinces.add(new CountryWithoutProvinces("province" + i));
        }
    }
    public ArrayList<Country> getProvinces() {
        return provinces;
    }
    public void addDailyStatistic(LocalDate date, int infections, int deaths){
        for(Country province : provinces){
            if(province instanceof CountryWithoutProvinces){
                ((CountryWithoutProvinces) province).addDailyStatistic(date, infections, deaths);
            }
        }
    }
    @Override
    public int getConfCases(LocalDate date){
        int sum = 0;
        for(Country province : provinces){
            sum += province.getConfCases(date);
        }
        return sum;
    }
    @Override
    public int getDeaths(LocalDate date){
        int sum = 0;
        for(Country province : provinces){
            sum += province.getDeaths(date);
        }
        return sum;
    }
}