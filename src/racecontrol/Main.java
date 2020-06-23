package racecontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

public class Main {

    //Carga de datos e inicio del menú
    public static void main(String[] args) {

        UI obj = new UI();

        try {
            Control.cargarCoches();
            Control.cargarCarreras();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        obj.menu();

    }

}
