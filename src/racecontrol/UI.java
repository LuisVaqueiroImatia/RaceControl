package racecontrol;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UI {

    static Scanner read = new Scanner(System.in);

    //menú de usuario
    public static void menu() {

        String opcion;
        System.out.println("++++++++++++++++BIENVENIDO, ESCOJA UNA OPCIÓN:++++++++++++++++");
        // no puede haber coches sin garaje
        System.out
                .println("--------------------------------\nCrearCarrera\nCrearTorneo\nMostrarCarreras\nMostrarTorneos\nIniciarCarrera\nIniciarTorneo\nRegistrarCoche\n"
                        + "BorrarCoche\nMostrarCoches\nSalir\nEscriba 'Ayuda' para ver la lista de comandos\n--------------------------------");

        do {

            opcion = read.nextLine();

            switch (opcion) {

                case "CrearCarrera":

                    Control.crearCarrera();

                    break;
                case "CrearTorneo":

                    Control.crearTorneo();

                    break;

                case "MostrarCarreras":

                    Control.mostrarCarreras();

                    break;

                case "MostrarTorneos":

                    Control.mostrarTorneos();

                    break;

                case "IniciarCarrera":

                    Control.iniciarCarrera();

                    break;

                case "IniciarTorneo":

                    Control.iniciarTorneo();

                    break;

                case "RegistrarCoche":

                    Control.crearCoche();

                    break;

                case "BorrarCoche":

                    Control.borrarCoche();

                    break;

                case "MostrarCoches":

                    Control.mostrarCoches();

                    break;
                /*Cada vez que salgamos del programa, se guardarán los coches 
                    y carreras o torneos en un json
                 */
                case "Salir":

                    System.out.println("Guardando datos...");

                     {
                        try {
                            Control.guardarCoches();
                            Control.guardarCarreras();
                        } catch (Exception ex) {
                            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    System.out.println("Saliendo del programa...");
                    System.exit(0);

                    break;

                case "Ayuda":
                    System.out
                            .println("--------------------------------\nCrearCarrera\nCrearTorneo\nMostrarCarreras\nMostrarTorneos\nIniciarCarrera\nIniciarTorneo\nRegistrarCoche\n"
                                    + "BorrarCoche\nMostrar Coches\nSalir\nEscriba 'Ayuda' para ver la lista de comandos\n--------------------------------");

                    break;

                default:
                    System.out.println("*ERROR*\nESCRIBA 'Ayuda' PARA MOSTRAR LAS OPCIONES VÁLIDAS");

            }

        } while (opcion.equals("Salir") == false);

    }

}
