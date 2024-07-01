import  java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Country {
    private final String name;
    private static String deathsDest;
    private static String casesDest;
    public String getName() {
        return name;
    }
    public Country(String name) {
        this.name = name;
    }
    public void addDailyStatistic(LocalDate date, int infections, int deaths) {
    }
    public static void setFile(String deathsDest, String casesDest) throws FileNotFoundException {
        File deathesFile = new File(deathsDest);
        File casesFile = new File(casesDest);
        if(!deathesFile.exists() || !deathesFile.canRead()){
            throw new FileNotFoundException("Nie mozna otworzyc plik");
        }
        if(!casesFile.exists() || !casesFile.canRead()){
            throw new FileNotFoundException("Nie mozna otworzyc plik");
        }
        Country.deathsDest = deathsDest;
        Country.casesDest = casesDest;
    }
    public static Country fromCsv(String countryName) throws IOException, CountryNotFoundException {
        BufferedReader deathsReader = new BufferedReader(new FileReader(deathsDest));
        BufferedReader casesReader = new BufferedReader(new FileReader(casesDest));
        String firstRowDeaths = deathsReader.readLine();
        String firstRowCases = casesReader.readLine();
        CountryColumns countryColumnsDeaths = getCountryColumns(firstRowDeaths, countryName);
        CountryColumns countryColumnsCases = getCountryColumns(firstRowCases, countryName);
        Country country;
        if(countryColumnsDeaths.columnCount == 1){
            country = new CountryWithoutProvinces(countryName);
        }
        else{
            country = new CountryWithProvinces(countryName, countryColumnsDeaths.columnCount);
        }
        String line;

        while ((line = deathsReader.readLine()) != null) {
            String[] data = line.split(",");
            LocalDate date = LocalDate.parse(data[0]);
            for(int i = 0; i < countryColumnsDeaths.columnCount; ++i){
                int deaths = Integer.parseInt(data[countryColumnsDeaths.firstColumnIndex + i]);
                int cases = Integer.parseInt(data[countryColumnsCases.firstColumnIndex + i]);
                if(country instanceof CountryWithoutProvinces){
                    ((CountryWithoutProvinces) country).addDailyStatistic(date, cases, deaths);
                }
                else{
                    ((CountryWithProvinces) country).getProvinces().get(i).addDailyStatistic(date, cases, deaths);
                }
            }

        }
        deathsReader.close();
        casesReader.close();
        return country;
    }
    public static Country[] fromCsv(String[] countryNames) throws IOException, CountryNotFoundException{
        ArrayList<Country> countries = new ArrayList<Country>();
        for(String countryName : countryNames){
            try{
                Country country = fromCsv(countryName);
                countries.add(country);
            } catch (CountryNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
        return countries.toArray(new Country[0]);
    }
    private static CountryColumns getCountryColumns(String firstRow, String countryName) throws CountryNotFoundException {
        String[] columns = firstRow.split(",");
        int firstColumnIndex = -1;
        int columnCount = 0;
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equals(countryName)) {
                if (firstColumnIndex == -1) {
                    firstColumnIndex = i;
                }
                columnCount++;
            }
        }
        if (firstColumnIndex == -1) {
            throw new CountryNotFoundException(countryName);
        }
        return new CountryColumns(firstColumnIndex, columnCount);

    }
    public static class CountryColumns{
        public final int firstColumnIndex;
        public final int columnCount;
        public CountryColumns(int firstColumnIndex, int columnCount) {
            this.firstColumnIndex = firstColumnIndex;
            this.columnCount = columnCount;
        }
    }
    abstract public int getConfCases(LocalDate date);
    abstract public int getDeaths(LocalDate date);
    public static void sortByDeaths(ArrayList<Country> countries, LocalDate startDate, LocalDate endDate){
        Collections.sort(countries, new Comparator<Country>(){
            @Override
            public int compare(Country c1, Country c2){
                int deaths1 = 0;
                int deaths2 = 0;
                for(LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)){
                    deaths1 += c1.getDeaths(date);
                    deaths2 += c2.getDeaths(date);
                }
                return Integer.compare(deaths1, deaths2);
            }
        });
    }
}