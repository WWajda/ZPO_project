import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

/**
 * Klasa Controller sluzy do projektowania
 * i opisu dzialania aplikacji
 * @author Weronika Wajda
 * @version 1.0 20/10/2019
 *
 */

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BarChart<String,Number> graph;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private MenuButton mbtnParametr;

    @FXML
    private MenuItem mitmPM10;

    @FXML
    private MenuItem mitmPM25;

    @FXML
    private MenuItem mitmSO2;

    @FXML
    private MenuItem mitmNO2;

    @FXML
    private MenuItem mitmO3;

    @FXML
    private MenuItem mitmCO;

    @FXML
    private Label lblNazwa;

    @FXML
    private TextField txtNazwa;

    @FXML
    private TextArea txtUwagi;

    @FXML
    private TextArea txtWyniki;

    @FXML
    private TextArea txtStan;

    @FXML
    private Button btnStan;

    @FXML
    private Button btnRysuj;

    @FXML
    private Button btnWyczysc;

    @FXML
    private Button btnZapisz;

    @FXML
    private Button btnWczytaj;

    @FXML
    private Circle bdb;

    @FXML
    private Circle db;

    @FXML
    private Circle um;

    @FXML
    private Circle dost;

    @FXML
    private Circle z;

    @FXML
    private Circle bz;

    /**
     *Obiekt klasy Parameters przechowujacy dane
     */
    private Parameters p = new Parameters();
    /**
     *Miasto, dla ktorego beda uzyskiwane dane
     */
    private String city;
    /**
     *Parametr jakosci powietrza, dla ktorego beda uzyskiwane dane
     */
    private String parameter;
    /**
     *Seria danych z wynikiem maksymalnym, minimalnym i srednim
     */
    static XYChart.Series series1 = new XYChart.Series();
    /**
     *Seria danych z gorna wartoscia graniczna normy
     */
    static XYChart.Series series2 = new XYChart.Series();
    /**
     *Tablica z nazwami stanow jakosci powietrza
     */
    static String[] stany = {"BARDZO Z\u0141Y","Z\u0141Y","DOSTATECZNY","UMIARKOWANY","DOBRY","BARDZO DOBRY"};


    /**
     * Funkcja wypelniajaca obiekt Parametres danymi,
     * ktore sa nastepnie przedstawiane w polach tekstowych
     * i formie graficznego wskaznika
     */
    @FXML
    void Oblicz(ActionEvent event) {
        Circle[] poziomy = {bz,z,dost,um,db,bdb};
        for (int i = 0; i < stany.length; i++) {
            poziomy[i].setVisible(false);
        }
        txtUwagi.setVisible(false);
        txtWyniki.clear();
        txtStan.clear();
        city = txtNazwa.getText();
            try {
                p.parametry(city, parameter);
                if(p.getStan()!=-1) {
                    txtWyniki.setText(p.toString());
                    txtStan.setText(stany[p.getStan()]);
                    for (int i = 0; i <= p.getStan(); i++) {
                        poziomy[i].setVisible(true);
                    }
                    btnRysuj.setDisable(false);
                    btnZapisz.setDisable(false);
                } else{
                    txtStan.setText("Brak indeksu");
                }
            } catch (IndexOutOfBoundsException | IOException | NullPointerException e) {
                txtUwagi.setVisible(true);
                txtUwagi.setText("Mozliwy brak danych pomiarowych! \nProsze sprawdzic poprawnosc nazwy miasta \ni/lub wybrac inny parametr");
            }
    }

    /**
     * Funkcja zapisujaca aktualnie pozyskane dane
     * w formacie json
     */
    @FXML
    void Save(ActionEvent event) {
        Gson gson = new Gson();
        Przetwarzanie.Save(gson.toJson(p));
    }

    /**
     * Funkcja odczytujaca dane z pliku tekstowego
     * i wypelniajaca obiekt Parametres danymi,
     * ktore sa nastepnie przedstawiane w polach tekstowych
     * i formie graficznego wskaznika
     */
    @FXML
    void Load(ActionEvent event) {
        Circle[] poziomy = {bz,z,dost,um,db,bdb};
        for (int i = 0; i < stany.length; i++) {
            poziomy[i].setVisible(false);
        }
        txtUwagi.setVisible(false);
        txtWyniki.clear();
        txtStan.clear();
        try {
            String dane = Przetwarzanie.Odczyt();
            p = p.parametry2(dane);
            txtWyniki.setText(p.toString());
            for (int i = 0; i <= p.getStan(); i++) {
                poziomy[i].setVisible(true);
            }
            txtStan.setText(stany[p.getStan()]);
            btnRysuj.setDisable(false);
            btnZapisz.setDisable(false);
        }catch (JsonSyntaxException e){
            txtUwagi.setVisible(true);
            txtUwagi.setText("Zly format danych");
        }
    }

    /**
     * Funkcja czyszczaca wykres i pola tekstowe
     * z pobranych danych. Przywraca stan poczatkowy.
     */
    @FXML
    void Clear(ActionEvent event) {
        Circle[] poziomy = {bz,z,dost,um,db,bdb};
        graph.getData().clear();
        series1.getData().clear();
        series2.getData().clear();
        for (int i = 0; i < stany.length; i++) {
            poziomy[i].setVisible(false);
        }
        txtUwagi.setVisible(false);
        txtWyniki.clear();
        txtStan.clear();
        btnWyczysc.setDisable(true);
        btnZapisz.setDisable(true);
        btnRysuj.setDisable(true);
    }


    /**
     * Funkcja pobierajaca dane i przedstawiajaca je
     * graficznie za pomoca wykresu slupkowego
     */
    @FXML
    void Draw(ActionEvent event) {

        btnWyczysc.setDisable(false);
        graph.getData().clear();
        series1.getData().clear();
        series2.getData().clear();
        double[] normy = p.normy(p.getParameter());
        series1.setName(p.getParameter()+" - "+p.getCity());
        series2.setName(p.getParameter()+" normy");
        series1.getData().add(new XYChart.Data("min", p.getMin()));
        series1.getData().add(new XYChart.Data("max", p.getMax()));
        series1.getData().add(new XYChart.Data("average", p.getMeasure()));
        series2.getData().add(new XYChart.Data(stany[p.getStan()], normy[normy.length-p.getStan()]));

        graph.getData().addAll(series1,series2);
        graph.setLegendVisible(true);
        xAxis.setLabel("Parameters");
        yAxis.setLabel("Value [\u00B5g/m\u00B3]");
    }

    /**
     * Funkcja przypisujaca odpowiednie instrukcje dzialania
     * do wszystkich obiektow typu MenuItem
     */
    void Przypisz(){
        ObservableList<MenuItem> list = mbtnParametr.getItems();
        for (int i = 0; i < list.size(); i++) {
            MenuItem itm = list.get(i);
            String parametr = itm.getText();
            itm.setOnAction(e -> {
                mbtnParametr.setText(parametr);
                parameter=parametr.toLowerCase().replace(",","");
            });
        }
    }


    @FXML
    void initialize() {
        assert lblNazwa != null : "fx:id=\"lblNazwa\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert txtNazwa != null : "fx:id=\"txtNazwa\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert mbtnParametr != null : "fx:id=\"mbtnParametr\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert mitmPM10 != null : "fx:id=\"mitmPM10\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert mitmPM25 != null : "fx:id=\"mitmPM25\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert mitmSO2 != null : "fx:id=\"mitmSO2\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert mitmNO2 != null : "fx:id=\"mitmNO2\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert mitmO3 != null : "fx:id=\"mitmO3\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert mitmCO != null : "fx:id=\"mitmCO\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert txtUwagi != null : "fx:id=\"txtUwagi\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert txtWyniki != null : "fx:id=\"txtWyniki\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert txtStan != null : "fx:id=\"txtStan\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert btnStan != null : "fx:id=\"btnStan\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert bz != null : "fx:id=\"bz\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert z != null : "fx:id=\"z\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert dost != null : "fx:id=\"dost\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert um != null : "fx:id=\"um\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert db != null : "fx:id=\"db\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert bdb != null : "fx:id=\"bdb\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert btnRysuj != null : "fx:id=\"btnRysuj\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert btnWyczysc != null : "fx:id=\"btnWyczysc\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert btnZapisz != null : "fx:id=\"btnZapisz\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert btnWczytaj != null : "fx:id=\"btnWczytaj\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert graph != null : "fx:id=\"graph\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert xAxis != null : "fx:id=\"xAxis\" was not injected: check your FXML file 'fxproject.fxml'.";
        assert yAxis != null : "fx:id=\"yAxis\" was not injected: check your FXML file 'fxproject.fxml'.";

        Przypisz();

    }
}
