import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Klasa Parameters sluzy do zbierania informacji
 * z pobranych pomiarow
 * Open AQ Platform API
 * @author Weronika Wajda
 * @version 1.0 20/10/2019
 *
 */

public class Parameters {

    /**
     *Kraj, w którym znajduje sie szukane miasto
     */
    private String country;
    /**
     *Miasto, dla ktorego beda uzyskiwane dane
     */
    private String city;
    /**
     *Data ostatniego pomiaru
     */
    private String date;
    /**
     *Czas ostatniego pomiaru
     */
    private String time;
    /**
     *Parametr jakosci powietrza, dla ktorego beda uzyskiwane dane
     */
    private String parameter;
    /**
     *Nazwy poszczegolnych lokalizacji, z ktorych pobrano dane
     */
    private ArrayList<String> locations;
    /**
     *Wyniki pomiarow z poszczegolnych lokalizacji
     */
    private ArrayList<Double> values;
    /**
     *Liczba pomiarow
     */
    private int number;
    /**
     *Maksymalny pomiar
     */
    private double max;
    /**
     *Minimalny pomiar
     */
    private double min;
    /**
     *Sredni pomiar
     */
    private double measure;
    /**
     *Odchylenie standardowe wyliczone ze wszystkich pomiarow
     */
    private double deviation;
    /**
     *Jednostka, w ktorej podawane sa wyniki pomiarow
     */
    private String unit;
    /**
     *Stan powietrza podany w skali 0-bardzo dobry ... 5-bardzo zly
     */
    private int stan;

    /**
     * Funkcja pobierajaca maksymalna wartosc pomiaru
     * @return max maksymalna wartosc pomiaru
     */
    public double getMax() {
        return max;
    }

    /**
     * Funkcja pobierajaca minimalna wartosc pomiaru
     * @return min minimalna wartosc pomiaru
     */
    public double getMin() {
        return min;
    }

    /**
     * Funkcja pobierajaca srednia wartosc pomiaru
     * @return measure srednia wartosc pomiaru
     */
    public double getMeasure() {
        return measure;
    }

    /**
     * Funkcja pobierajaca stan powietrza w skali liczbowej
     * @return stan stan powietrza w skali
     */
    public int getStan(){
        return stan;
    }

    /**
     * Funkcja pobierajaca nazwe parameteru
     * @return parameter nazwa parametru
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Funkcja pobierajaca nazwe miasta
     * @return city nazwa miasta
     */
    public String getCity() {
        return city;
    }

    /**
     * Funkcja zwracajaca tekstowa reprezentacje uzyskanych wynikow pomiaru i najwazniejszych danych
     * @return wyniki uzyskane wyniki i dane pomiaru
     */
    @Override
    public String toString() {
        String wyniki;
        wyniki = "country: " + country + '\n' +
                "city: " + city + '\n' +
                "date: " + date + '\n' +
                "time: " + time + '\n' +
                "parameter: " + parameter + '\n' +
                "max: " + max + unit + '\n' +
                "min: " + min + unit + '\n' +
                "average: " + measure + unit + '\n' +
                "deviation: " + deviation + unit + '\n'+
                "measures:"+ '\n';
        for (int i = 0; i < number; i++) {
            wyniki = wyniki  + locations.get(i) + " - " + values.get(i) + unit + '\n';
        }

        return wyniki;
    }

    /**
     * Funkcja pobierajaca dane z serwera za pomoca odpowiedniego zapytania
     * i przypisujaca uzyskane dane do odpowiednich pol klasy
     * @param m nazwa miasta, dla ktorego chcemy uzyskac dane
     * @param p nazwa parametru, dla ktorego chcemy uzyskac dane
     * @throws IOException
     * @throws IndexOutOfBoundsException
     */
    public void parametry(String m, String p) throws IOException, IndexOutOfBoundsException {
            Gson gson = new Gson();
            Zapytanie z = new Zapytanie();
                String jsonRepresentation = String.valueOf(z.zapytanie(m, p));
                JsonObject object = gson.fromJson(jsonRepresentation, JsonObject.class);
                JsonArray array = object.get("results").getAsJsonArray();
                city = z.getCity();
                String [] dit = z.getDate().split("T");
                date = dit[0];
                time = dit[1].substring(0,5);
                parameter = z.getParameter();
                number = array.size();
                JsonObject object1;
                double suma = 0;
                double suma2 = 0;
                ArrayList<String> loc = new ArrayList<>();
                ArrayList<Double> val = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    object1 = (JsonObject) array.get(i);
                    loc.add(String.valueOf(object1.get("location")));
                    val.add(Double.valueOf(String.valueOf(object1.get("value"))));
                    suma += val.get(i);
                    country = object1.get("country").getAsString();
                }

                unit = "\u00B5g/m\u00B3";
                locations = (ArrayList<String>) loc.clone();
                values = (ArrayList<Double>) val.clone();
                Collections.sort(val);
                min = val.get(0);
                max = val.get(number - 1);
                measure = Double.valueOf(String.format("%.2f", suma / number).replace(",", "."));
                for (int i = 0; i < array.size(); i++) {
                    suma2 += Math.pow(val.get(i) - measure, 2);
                }
                deviation = Double.valueOf(String.format("%.3f", Math.sqrt(suma2 / (number - 1))).replace(",", "."));
                stan(normy(parameter));
    }

    /**
     * Funkcja przetwarzajaca dane z postaci tekstowej w formacie json
     * na pojedyncze wartosci przypisywane do odpowiednich pol obiektu
     * klasy Parameters
     * @param dane dane tekstowe w formacie json
     * @return p obiekt klasy Parameters z przypisanymi wartosciami pol
     */
    public Parameters parametry2(String dane) {
        Gson gson = new Gson();
        Parameters p = gson.fromJson(dane, Parameters.class);
        return p;
    }

    /**
     * Funkcja pobierajaca tablice z wartosciami granicznymi
     * norm jakosci powietrza dla odpowiednich parametrow
     * @param parameter nazwa parametru
     * @return normy wrtosci graniczne norm
     */
    public double[] normy(String parameter){
        double[] normy = new double[5];
        switch (parameter){
            case "pm10":
                normy= new double[]{0,21,61,101,141,201};
                break;
            case "pm25":
                normy= new double[]{0,13,37,61,85,121};
                break;
            case "o3":
                normy= new double[]{0,71,121,151,181,241};
                break;
            case "no2":
                normy= new double[]{0,41,101,151,201,401};
                break;
            case "so2":
                normy= new double[]{0,51,101,201,351,501};
                break;
        }

        return normy;
    }

    /**
     * Funkcja przypisujaca wartosc liczbowa do
     * pola klasy stan na podstawie norm granicznych
     * @param normy wrtosci graniczne norm dla danego parametru
     */
    public void stan(double[] normy){
        if(measure<normy[0]){
            stan=-1;
        }else if(measure<normy[1] && measure>=normy[0])
            stan=5;
        else if (measure<normy[2])
            stan=4;
        else if (measure<normy[3])
            stan=3;
        else if (measure<normy[4])
            stan=2;
        else if (measure<normy[5])
            stan=1;
        else if (measure>normy[5])
            stan=0;
    }

}

