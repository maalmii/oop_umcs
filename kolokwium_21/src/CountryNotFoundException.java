public class CountryNotFoundException extends Exception{
public CountryNotFoundException(String countryName){

    super("Country " + countryName + " not found in the file");
    }
}
