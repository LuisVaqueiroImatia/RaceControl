package racecontrol;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Carrera {

    Scanner read = new Scanner(System.in);

    private ArrayList<Coche> participantes = new ArrayList<Coche>();

    private ArrayList<Coche> ganadores = new ArrayList<Coche>();

    private String nombrePremio;

    private TipoCarrera tipo;

    public Carrera(ArrayList<Coche> participantes, String nombrePremio, TipoCarrera tipo) {

        this.participantes = participantes;
        this.nombrePremio = nombrePremio;
        this.tipo = tipo;
    }

    enum TipoCarrera {
        Standard, Eliminacion, TorneoStandard, TorneoEliminacion
    }

    // Método para ejecutar carreras
    /*
    Standard: se calculan distancias percorridas por cada coche de manera aleatoria
    El que más distancia haya percorrido, gana
    
    Eliminación: se calculan distancias percorridas por cada coche de manera aleatoria
    en cada vuelta. El que haya percorrido la menor distancia queda eliminado
     */
    public void ejecutarCarrera() {

        // Tiene que haber como mínimo 3 participantes
        if (participantes.size() >= 3) {

            if (this.tipo == TipoCarrera.Standard) {
                System.out.println("Ejecutando carrera Standard '" + this.nombrePremio + "' :");

                // Insertamos números de distancia percorrida aleatorios
                for (Coche c : participantes) {

                    c.setDistanciaPercorridaKm((int) (Math.random() * (1000 - 1 + 1) + 1));

                }

                // Mostramos resultados ordenados según la distancia percorrida
                Collections.sort(participantes);

                System.out.println("RESULTADOS CARRERA STANDARD '" + this.nombrePremio + "' :");
                System.out.println("------------------------------------------------");
                for (Coche c : participantes) {
                    System.out.println(
                            "ID: " + c.getId() + "\nDistancia percorrida (Km): " + c.getDistanciaPercorridaKm());
                    System.out.println("------------------------------------------------");
                }
                System.out.println("------------------------------------------------");
                System.out.println("GANADORES CARRERA '" + this.nombrePremio + "' :"
                        + "\n*******************************************");

                // MOSTRAR GANADORES
                System.out.println("1º PUESTO: " + participantes.get(0));
                System.out.println("2º PUESTO: " + participantes.get(1));
                System.out.println("3º PUESTO: " + participantes.get(2));
                System.out.println();

                //AÑADIMOS LOS GANADORES AL ARRAY
                ganadores.add(participantes.get(0));
                ganadores.add(participantes.get(1));
                ganadores.add(participantes.get(2));

                //EXPORTAMOS RESULTADOS SI QUEREMOS
                String opcion;
                do {
                    System.out.println("Desea exportar los resultados? (si/no)");
                    opcion = read.nextLine();

                    switch (opcion) {

                        case "si": {
                            try {
                                exportarResultados();
                            } catch (Exception ex) {
                                Logger.getLogger(Carrera.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        break;

                        case "no":

                            System.out.println("Finalizando...");

                            break;

                        default:
                            System.out.println("*ERROR*\nESCOJA UNA OPCIÓN CORRECTA");

                    }
                } while (((opcion.equals("si") == false)) && ((opcion.equals("no") == false)));

            } else if (this.tipo == TipoCarrera.Eliminacion) {

                System.out.println("Ejecutando carrera Eliminacion '" + this.nombrePremio + "' :");

                int vueltas = participantes.size() - 1;
                ArrayList<Coche> ganadoresE = new ArrayList<Coche>();
                // Ponemos los participantes en otro array
                ganadoresE.addAll(participantes);

                // Cada vuelta calculamos una distancia aleatoria percorrida por cada coche
                // El coche que menos distancia recorra, sale del array de ganadores
                for (int i = 0; i < vueltas; i++) {

                    for (Coche c : ganadoresE) {

                        c.setDistanciaPercorridaKm((int) (Math.random() * (1000 - 1 + 1) + 1));

                    }

                    Collections.sort(ganadoresE);

                    System.out.println("RESULTADOS VUELTA " + (i + 1));

                    System.out.println("------------------------------------------------");
                    for (Coche c : ganadoresE) {
                        System.out.println(
                                "ID: " + c.getId() + "\nDistancia percorrida (Km): " + c.getDistanciaPercorridaKm());
                        System.out.println("------------------------------------------------");
                    }
                    System.out.println("------------------------------------------------");

                    // ELIMINAMOS AL ÚLTIMO
                    System.out.println("ELIMINADO: " + Collections.max(ganadoresE));
                    ganadoresE.remove(Collections.max(ganadoresE));

                }

                System.out.println("****************************************\nGANADOR DE LA CARRERA ELIMINATORIA '"
                        + this.nombrePremio + "' : ");
                System.out.println(ganadoresE);

                ganadores.add(ganadoresE.get(0));

                //Exportamos los resultados si queremos a un json
                String opcion;
                do {
                    System.out.println("Desea exportar los resultados? (si/no)");
                    opcion = read.nextLine();

                    switch (opcion) {

                        case "si": {
                            try {
                                exportarResultados();
                            } catch (Exception ex) {
                                Logger.getLogger(Carrera.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        break;

                        case "no":

                            System.out.println("Finalizando...");

                            break;

                        default:
                            System.out.println("*ERROR*\nESCOJA UNA OPCIÓN CORRECTA");

                    }
                } while (((opcion.equals("si") == false)) && ((opcion.equals("no") == false)));

            } else {
                System.out.println("*ERROR*/n LA CARRERA SELECCIONADA ES UN TORNEO");
            }

        } else {
            System.out.println("*ERROR*\nDEBE DE HABER COMO MÍNIMO 3 PARTICIPANTES EN LA CARRERA");
        }

        // REINICIAMOS DISTANCIAS DE LOS PARTICIPANTES
        for (Coche c : participantes) {

            c.setDistanciaPercorridaKm(0);

        }
    }

    // Como una carrera, pero se ejecuta 10 veces e implementa un sistema de puntos
    /*
    Las carreras son ejecutadas de la misma manera que en el método anterior
    Se ha implementado un sistema de puntos:
    Standard: En cada carrera, 3 puntos para el primero, 2 para el segundo y
    1 para el último
    
    Eliminación:  En cada carrera, 3 puntos para el ganador, 2 para el último eliminado
    y 1 para el penúltimo eliminado
    
    Ganador del torneo: quién más puntos acumule a lo largo de las 10 carreras
     */
    public void ejecutarTorneo() {

        // Tiene que haber como mínimo 3 participantes
        if (participantes.size() >= 3) {

            if (this.tipo == TipoCarrera.TorneoStandard) {

                System.out.println("Ejecutando torneo Standard '" + this.nombrePremio + "' :");

                for (int i = 0; i < 10; i++) {

                    System.out.println("CARRERA " + (i + 1));

                    // Insertamos números de distancia percorrida aleatorios
                    for (Coche c : participantes) {

                        c.setDistanciaPercorridaKm((int) (Math.random() * (1000 - 1 + 1) + 1));

                    }

                    // Mostramos resultados ordenados según la distamcia percorrida
                    Collections.sort(participantes);

                    System.out.println("RESULTADOS CARRERA " + (i + 1));
                    System.out.println("------------------------------------------------");
                    for (Coche c : participantes) {
                        System.out.println(
                                "ID: " + c.getId() + "\nDistancia percorrida (Km): " + c.getDistanciaPercorridaKm());
                        System.out.println("------------------------------------------------");
                    }
                    System.out.println("------------------------------------------------");
                    System.out
                            .println("GANADORES CARRERA " + (i + 1) + "\n*******************************************");

                    // Mostrar ganadores
                    System.out.println("1º PUESTO: " + participantes.get(0) + "PUNTOS GANADOS: 3");
                    System.out.println("2º PUESTO: " + participantes.get(1) + "PUNTOS GANADOS: 2");
                    System.out.println("3º PUESTO: " + participantes.get(2) + "PUNTOS GANADOS: 1");
                    System.out.println();
                    // Poner puntos a los ganadores
                    participantes.get(0).setPuntos(participantes.get(0).getPuntos() + 3);
                    participantes.get(1).setPuntos(participantes.get(1).getPuntos() + 2);
                    participantes.get(2).setPuntos(participantes.get(2).getPuntos() + 1);

                }

                Coche ganador = new Coche();
                ganador.setPuntos(0);
                Coche ganador2 = new Coche();
                ganador2.setPuntos(0);
                //Comprobamos si hay empate de manera simple
                for (Coche c : participantes) {
                    if (c.getPuntos() > ganador.getPuntos()) {

                        ganador = c;

                    } else if (c.getPuntos() == ganador.getPuntos()) {

                        ganador2 = c;

                    }
                }

                System.out.println("**********************************************\nGANADOR DEL TORNEO: ");
                if (ganador != ganador2) {
                    System.out.println(ganador + " CON " + ganador.getPuntos() + " PUNTOS !!!");

                    //AÑADIMOS LOS GANADORES AL ARRAY
                    ganadores.add(ganador);

                } else {

                    System.out.println("EMPATE ENTRE " + ganador + " Y " + ganador2 + " CON " + ganador.getPuntos());
                    //AÑADIMOS LOS GANADORES AL ARRAY
                    ganadores.add(ganador);
                    ganadores.add(ganador2);
                }

                //EXPORTAMOS RESULTADOS SI QUEREMOS
                String opcion;
                do {
                    System.out.println("Desea exportar los resultados? (si/no)");
                    opcion = read.nextLine();

                    switch (opcion) {

                        case "si": {
                            try {
                                exportarResultados();
                            } catch (Exception ex) {
                                Logger.getLogger(Carrera.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        break;

                        case "no":

                            System.out.println("Finalizando...");

                            break;

                        default:
                            System.out.println("*ERROR*\nESCOJA UNA OPCIÓN CORRECTA");

                    }
                } while (((opcion.equals("si") == false)) && ((opcion.equals("no") == false)));

            } else if (this.tipo == TipoCarrera.TorneoEliminacion) {

                System.out.println("Ejecutando torneo Eliminacion '" + this.nombrePremio + "' :");

                ArrayList<Coche> ganadoresTE = new ArrayList<Coche>();

                for (int j = 0; j < 10; j++) {

                    System.out.println("CARRERA " + (j + 1));

                    ganadoresTE.clear();
                    ganadoresTE.addAll(participantes);

                    int vueltas = participantes.size() - 1;

                    for (int i = 0; i < vueltas; i++) {

                        for (Coche c : ganadoresTE) {

                            c.setDistanciaPercorridaKm((int) (Math.random() * (1000 - 1 + 1) + 1));

                        }

                        Collections.sort(ganadoresTE);

                        System.out.println("RESULTADOS VUELTA " + (i + 1));

                        System.out.println("------------------------------------------------");
                        for (Coche c : ganadoresTE) {
                            System.out.println("ID: " + c.getId() + "\nDistancia percorrida (Km): "
                                    + c.getDistanciaPercorridaKm());
                            System.out.println("------------------------------------------------");
                        }
                        System.out.println("------------------------------------------------");

                        if (ganadoresTE.size() == 3) {
                            System.out.println("TERCER PUESTO PARA: " + Collections.max(ganadoresTE).getId() + "(PUNTOS GANADOS: 1)");
                            Collections.max(ganadoresTE).setPuntos(Collections.max(ganadoresTE).getPuntos() + 1);
                        } else if (ganadoresTE.size() == 2) {
                            System.out.println("SEGUNDO PUESTO PARA: " + Collections.max(ganadoresTE).getId() + "(PUNTOS GANADOS: 2)");
                            Collections.max(ganadoresTE).setPuntos(Collections.max(ganadoresTE).getPuntos() + 2);

                        }

                        System.out.println("ELIMINADO: " + Collections.max(ganadoresTE));
                        ganadoresTE.remove(Collections.max(ganadoresTE));

                    }

                    System.out.println("******************************************\nGANADOR DE LA CARRERA ELIMINATORIA "
                            + (j + 1));
                    System.out.println(ganadoresTE + " (PUNTOS GANADOS: 3)");
                    // ponemos 3 puntos al ganador
                    Collections.max(ganadoresTE).setPuntos(Collections.max(ganadoresTE).getPuntos() + 3);

                }

                // Una vez finalizado el torneo, comprobamos si hay empate o no
                //y anunciamos ganadore
                Coche ganador = new Coche();
                ganador.setPuntos(0);
                Coche ganador2 = new Coche();
                ganador2.setPuntos(0);

                for (Coche c : participantes) {
                    if (c.getPuntos() > ganador.getPuntos()) {

                        ganador = c;

                    } else if (c.getPuntos() == ganador.getPuntos()) {

                        ganador2 = c;

                    }
                }

                System.out.println("**************************************\nGANADOR DEL TORNEO: ");
                if (ganador != ganador2) {
                    System.out.println(ganador + " CON " + ganador.getPuntos() + " PUNTOS !!!");
                    //AÑADIMOS LOS GANADORES AL ARRAY
                    ganadores.add(ganador);
                } else {

                    System.out.println("EMPATE ENTRE " + ganador + " Y " + ganador2 + " CON " + ganador.getPuntos());
                    //AÑADIMOS LOS GANADORES AL ARRAY
                    ganadores.add(ganador);
                    ganadores.add(ganador2);
                }

                //EXPORTAMOS RESULTADOS SI QUEREMOS
                String opcion;
                do {
                    System.out.println("Desea exportar los resultados? (si/no)");
                    opcion = read.nextLine();

                    switch (opcion) {

                        case "si": {
                            try {
                                exportarResultados();
                            } catch (Exception ex) {
                                Logger.getLogger(Carrera.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        break;

                        case "no":

                            System.out.println("Finalizando...");

                            break;

                        default:
                            System.out.println("*ERROR*\nESCOJA UNA OPCIÓN CORRECTA");

                    }
                } while (((opcion.equals("si") == false)) && ((opcion.equals("no") == false)));

            } else {
                System.out.println("*ERROR*/n EL TORNEO SELECCIONADO ES UNA CARRERA");
            }

        } else {
            System.out.println("*ERROR*\nDEBE DE HABER COMO MÍNIMO 3 PARTICIPANTES EN EL TORNEO");
        }

        // REINICIAMOS DISTANCIAS Y PUNTOS DE LOS PARTICIPANTES 
        for (Coche c : participantes) {

            c.setDistanciaPercorridaKm(0);
            c.setPuntos(0);

        }

    }

    // GS
    public String getNombrePremio() {
        return nombrePremio;
    }

    public void setNombrePremio(String nombrePremio) {
        this.nombrePremio = nombrePremio;
    }

    public TipoCarrera getTipo() {
        return tipo;
    }

    public void setTipo(TipoCarrera tipo) {
        this.tipo = tipo;
    }

    public ArrayList<Coche> getParticipantes() {
        return participantes;
    }

    // Para exportar los resultados de una carrera o torneo
    public void exportarResultados() throws Exception {

        System.out.println("Introduce el nombre del fichero a crear: ");
        String fichero = read.nextLine();

        JSONObject objetoCarrera = new JSONObject();
        objetoCarrera.put("nombrePremio", this.nombrePremio);
        objetoCarrera.put("tipo", this.tipo);

        JSONArray participantesC = new JSONArray();
        participantesC.addAll(this.participantes);

        objetoCarrera.put("participantes", participantesC);

        JSONArray ganadoresC = new JSONArray();
        ganadoresC.addAll(this.ganadores);

        objetoCarrera.put("ganadores", ganadoresC);

        Files.write(Paths.get(fichero), objetoCarrera.toJSONString().getBytes());

    }

    @Override
    public String toString() {
        return "Carrera{"
                + "nombrePremio='" + nombrePremio + '\''
                + ", tipo=" + tipo
                + ", participantes=" + participantes
                + '}';
    }

}
