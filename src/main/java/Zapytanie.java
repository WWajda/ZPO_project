import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Klasa Zapytanie sluzy do tworzenia zapytania
 * w postaci linku wysylanego do serwera
 * Open AQ Platform API
 * @author Weronika Wajda
 * @version 1.0 20/10/2019
 *
 */

public class Zapytanie {

    /**
     *Miasto, dla ktorego beda uzyskiwane dane
     */
    private String city;
    /**
     *Parametr jakosci powietrza, dla ktorego beda uzyskiwane dane
     */
    private String parameter;
    /**
     *Data ostatniego pomiaru
     */
    private String date;

    /**
     * Funkcja pobierajaca nazwe miasta
     * @return city nazwa miasta
     */
    public String getCity() {
        return city;
    }

    /**
     * Funkcja pobierajaca nazwe parameteru
     * @return parameter nazwa parametru
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Funkcja pobierajaca tekstowa reprezentacje daty
     * @return date tekstowa reprezentacja daty
     */
    public String getDate() {
        return date;
    }

    /**
     * Funkcja odczytuje tekstowa reprezentacje linku url
     * i wysylanie zapytania do serwera
     * @param url tekstowa reprezentacja zapytania
     * @return response odpowiedz z serwera
     * @throws IOException
     */
    public StringBuffer odczyt(String url) throws IOException{
        StringBuffer response = new StringBuffer();
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        return response;
    }

    /**
     * Funkcja tworzy tekstowa reprezentacje
     * zapytania wysylanego potem do serwera
     * @param m nazwa miasta, dla ktorego chcemy uzyskac zapytanie
     * @param p nazwa parametru, dla ktorego chcemy uzyskac zapytanie
     * @return zapytanie tekstowa reprezentacja linku z zapytaniem
     * @throws IOException
     * @throws IndexOutOfBoundsException
     */
    public String zapytanie(String m, String p) throws IOException, IndexOutOfBoundsException {
        StringBuilder link = new StringBuilder();
        link.append("https://api.openaq.org/v1/measurements?city="+m);
        link.append("&parameter="+p);
        String url = link.toString();
        Gson gson = new Gson();
        String jsonRepresentation;
        jsonRepresentation = String.valueOf(odczyt(url));
        JsonObject object = gson.fromJson(jsonRepresentation, JsonObject.class);
        JsonArray array = object.get("results").getAsJsonArray();
        JsonObject object1 = (JsonObject) array.get(0);
        JsonObject object2 = (JsonObject) object1.get("date");
        String time = object2.get("utc").getAsString();
        link.append("&date_from="+time);
        String newURL = link.toString();
        city=m;
        parameter=p;
        date=time;
        String zapytanie=String.valueOf(odczyt(newURL));
        return zapytanie;

    }
}

