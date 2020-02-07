import java.awt.*;
import java.io.*;

/**
 * Klasa Przetwarzanie sluzy do przetwarzania,
 * czyli zapisu lub odczytu dannych w formacie json
 * uzyskanych z dzialania programu 'Stan Powietrza'.
 *
 * @author Weronika Wajda
 * @version 1.0 01/02/2020
 *
 */

public class Przetwarzanie {

    /**
     * Funkcja sluzy do zapisu do pliku .txt danych w formacie json.
     * @param dane dane do zapisu do pliku
     */
    public static void Save(String dane) {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        Frame a = new Frame();
        a.setBounds(20,20,400,500);
        a.setVisible(false);

        FileDialog fd =new FileDialog(a,"Zapisz",FileDialog.SAVE);
        fd.setVisible(true);
        String katalog=fd.getDirectory();
        String plik=fd.getFile();
        String pathSavedFile = katalog + plik + ".txt";
        try {
            fileWriter = new FileWriter(pathSavedFile);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(dane));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Funkcja sluzy do odczytu z pliku .txt danych w formacie json.
     * @return  dane dane uzyskane z pliku
     */
    public static String Odczyt() {
        String dane=null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Frame a = new Frame();
        a.setBounds(20,20,400,500);
        a.setVisible(false);

        FileDialog fd =new FileDialog(a,"Wczytaj",FileDialog.LOAD);
        fd.setVisible(true);
        String katalog=fd.getDirectory();
        String plik=fd.getFile();
        String pathSavedFile = katalog + plik;
        try {
            fileReader = new FileReader(pathSavedFile);
            bufferedReader = new BufferedReader(fileReader);
            dane = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedReader.close();
            fileReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dane;
    }
}
