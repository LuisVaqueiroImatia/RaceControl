package racecontrol;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
Clase donde están todas las funciones útiles
 */
public class Control {

    // Para almacenar coches
    static ArrayList<Coche> coches = new ArrayList<Coche>();

    // Para almacenar carreras o torneos creados
    static ArrayList<Carrera> carreras = new ArrayList<Carrera>();

    static Scanner read = new Scanner(System.in);

    // Para buscar si ya existe ese id entre los coches registrados
    public static boolean buscarIdCoche(String id) {

        boolean existe = false;

        for (Coche c : coches) {

            if (c.getId().equals(id)) {
                existe = true;
                break;
            }

        }

        return existe;

    }

    // Para crear coches y almacenarlos en el array
    // Control de errores para el ID
    public static void crearCoche() {

        String id = "";
        String marca = "";
        String escuderia = "";

        do {
            System.out.println("--------------------------------------------\nCreando coche:");

            System.out.println("Introduce el ID del coche:");
            id = read.nextLine();

            if (id.equals("") == false) {

                if (buscarIdCoche(id) == false) {

                    System.out.println("Introduce la marca del coche:");
                    marca = read.nextLine();
                    System.out.println("Introduce el modelo del coche:");
                    String modelo = read.nextLine();
                    System.out.println("Introduce la escudería del garaje al que pertenece el coche:");
                    escuderia = read.nextLine();

                    Coche coche = new Coche(id, marca, modelo, escuderia);

                    coches.add(coche);

                    System.out.println("Coches registrados:");
                    mostrarCoches();

                    break;

                } else {

                    System.out.println("*ERROR*\nEl ID del coche ya existe");

                }
            } else {
                System.out.println("*ERROR*\nEl ID del coche no puede estar en blanco");
            }
        } while ((id.equals("")) || (buscarIdCoche(id) == true));

        System.out.println("--------------------------------------------");

    }

    // Para comprobar si la escuderia existe
    public static boolean buscarEscuderia(String escuderia) {

        boolean existe = false;

        for (Coche c : coches) {
            if (c.getEscuderia().equals(escuderia)) {

                existe = true;
                break;
            }

        }

        return existe;

    }

    // Para borrar un coche del array de coches
    public static void borrarCoche() {

        System.out.println("--------------------------------------------\nIntroduce el ID del coche a borrar:");
        String id = read.nextLine();

        if (buscarIdCoche(id) == true) {

            System.out.println("Borrando coche...");

            for (Coche c : coches) {

                if (c.getId().equals(id)) {

                    coches.remove(c);
                    break;
                }

            }
        } else {
            System.out.println("*ERROR*\nID de coche inexistente");
        }

        System.out.println("--------------------------------------------");

    }

    // Para mostrar todos los coches
    public static void mostrarCoches() {

        System.out.println("--------------------------------------------\nMostrando coches registrados:");

        for (Coche c : coches) {
            System.out.println(c);
        }

        System.out.println("--------------------------------------------");

    }

    // Para crear carreras
    // Control de errores para el nombre del premio y el número de participantes
    public static void crearCarrera() {

        String premio = "";

        boolean marca = false;

        do {
            System.out.println("--------------------------------------------\nIntroduce el nombre del premio: ");

            premio = read.nextLine();

            if (premio.equals("") == false) {

                if (buscarCarrera(premio) == false) {

                    // traducir
                    System.out.println("Qué tipo de carrera será? (Standard/Eliminacion)");

                    String tipo = read.nextLine();

                    System.out.println("Introduzca los participantes de la carrera: ");

                    switch (tipo) {

                        case "Standard":

                            ArrayList<Coche> participantesS = new ArrayList<Coche>();

                            try {
                                participantesS = anhadirParticipantes();
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("*ERROR*\n NO HAY NINGÚN COCHE REGISTRADO O SON INSUFICIENTES");
                            }

                            if (participantesS.size() >= 3) {

                                Carrera carreraS = new Carrera(participantesS, premio, Carrera.TipoCarrera.Standard);

                                carreras.add(carreraS);

                                System.out.println("Carrera Standard " + premio + " creada !");
                                marca = true;

                            } else {
                                System.out.println("*ERROR*\nTiene que haber al menos 3 participantes en la carrera");
                            }

                            break;

                        case "Eliminacion":

                            ArrayList<Coche> participantesE = new ArrayList<Coche>();
                            try {
                                participantesE = anhadirParticipantes();
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("*ERROR*\n NO HAY NINGÚN COCHE REGISTRADO O SON INSUFICIENTES");
                            }

                            if (participantesE.size() >= 3) {

                                Carrera carreraE = new Carrera(participantesE, premio, Carrera.TipoCarrera.Eliminacion);

                                carreras.add(carreraE);

                                System.out.println("Carrera Eliminacion " + premio + " creada !");

                                marca = true;

                            } else {
                                System.out.println("*ERROR*\nTiene que haber al menos 3 participantes en la carrera");
                            }

                            break;

                        default:
                            System.out.println("*ERROR*\nINTRODUCE UN TIPO DE CARRERA VÁLIDO (Standard/Eliminacion)");

                    }

                } else {
                    System.out.println("*ERROR*\nNOMBRE DE PREMIO YA EXISTENTE");

                }
            } else {
                System.out.println("*ERROR*\nEl nombre del premio no puede estar en blanco");

            }

        } while ((premio.equals("")) || ((buscarCarrera(premio) == true) && (!marca)));

        System.out.println("--------------------------------------------");

    }

    // Para añadir participantes a una carrera
    // Control de errores hecho en "crearCarrera"
    public static ArrayList<Coche> anhadirParticipantes() {

        ArrayList<Coche> participantes = new ArrayList<>();

        System.out.println("De un garaje o de todos los garajes? Por favor, seleccione: (uno/todos)");

        String opcion = read.nextLine();

        switch (opcion) {

            case "uno":

                System.out.println("Introduzca el nombre de la escudería participante: ");
                String escuderia = read.nextLine();

                if (buscarEscuderia(escuderia)) {

                    for (Coche c : coches) {

                        if (c.getEscuderia().equals(escuderia)) {

                            participantes.add(c);

                        }

                    }

                } else {
                    System.out.println("*ERROR*\n ESCUDERIA INEXISTENTE");
                }

                break;

            case "todos":

                System.out.println("Añadiendo un participante aleatorio de cada garaje...");

                /*
                Se generará un número aleatorio en base a la longitud del array coches, cada
		vez que se saque por su index un coche, se seleccionará como participante y
		se meterá su escudería en un array que se usará para ir comprobando si ya hay otro coche
                de esa escudería o no
                 */
                int nescuderias = contarEscuderias();
                ArrayList<String> escuderias = new ArrayList<>();

                do {

                    int indexRandom = (int) (Math.random() * (coches.size()));

                    Coche participante = coches.get(indexRandom);

                    if (escuderias.contains(participante.getEscuderia())) {
                        continue;
                    } else {

                        escuderias.add(participante.getEscuderia());
                        participantes.add(participante);

                    }

                } while (escuderias.size() != nescuderias);

                break;

            default:
                System.out.println("*ERROR*\nINTRODUZCA UNA OPCIÓN CORRECTA (uno/todos)");

        }

        return participantes;

    }

    // Para contar cuantas escuderias hay en el array coches
    public static int contarEscuderias() {

        int n = 0;

        ArrayList<String> escuderias = new ArrayList<String>();

        for (Coche c : coches) {

            if (escuderias.contains(c.getEscuderia())) {

                continue;

            } else {

                escuderias.add(c.getEscuderia());
                n++;

            }

        }

        return n;

    }

    // Para mostrar las carreras creadas
    public static void mostrarCarreras() {
        System.out.println("--------------------------------------------\nMostrando carreras creadas...");
        for (Carrera c : carreras) {

            if ((c.getTipo() == Carrera.TipoCarrera.Standard) || (c.getTipo() == Carrera.TipoCarrera.Eliminacion)) {
                System.out.println(c);
            }

        }

        System.out.println("--------------------------------------------");

    }

    // Para comprobar si una carrera existe o no en el array carreras
    public static boolean buscarCarrera(String carrera) {

        Boolean existe = false;

        for (Carrera c : carreras) {

            // Comprobamos que sea una carrera y no un torneo
            if ((carrera.equals(c.getNombrePremio())) && ((c.getTipo() == Carrera.TipoCarrera.Standard)
                    || (c.getTipo() == Carrera.TipoCarrera.Eliminacion))) {

                existe = true;

            }

        }

        return existe;

    }

    // Para comprobar si un torneo existe o no en el array carreras
    public static boolean buscarTorneo(String torneo) {

        Boolean existe = false;

        for (Carrera c : carreras) {

            // Comprobamos que sea un torneo y no una carrera
            if ((torneo.equals(c.getNombrePremio())) && ((c.getTipo() == Carrera.TipoCarrera.TorneoEliminacion)
                    || (c.getTipo() == Carrera.TipoCarrera.TorneoStandard))) {

                existe = true;

            }

        }

        return existe;

    }

    // Para iniciar carreras accediento a su método de clase
    public static void iniciarCarrera() {

        System.out.println("--------------------------------------------\nIntroduce el nombre del Premio de la carrera que se va a iniciar: ");
        String premio = read.nextLine();

        if (buscarCarrera(premio)) {

            for (Carrera c : carreras) {

                if (premio.equals(c.getNombrePremio())) {

                    c.ejecutarCarrera();

                    break;

                }

            }

        } else {

            System.out.println("*ERROR*\nEL NOMBRE DEL PREMIO INTRODUCIDO NO EXISTE");

        }

        System.out.println("--------------------------------------------");

    }

    /*  
     Para crear torneos
     Pondremos un nombre al torneo, que será el nombre del premio que usarán las
     10 carreras que se ejecutarán en él 
     Control de errores en el nombre del torneo y el número de participantes
     */
    public static void crearTorneo() {

        Boolean marca = false;

        String nombreTorneo = "";

        do {

            System.out.println("--------------------------------------------\nIntroduzca el nombre del torneo:");
            nombreTorneo = read.nextLine();

            if (nombreTorneo.equals("") == false) {

                // Comprobamos que no haya una carrera con el mismo nombre
                if (buscarTorneo(nombreTorneo) == false) {

                    System.out.println("Qué tipo de torneo será?\t Standard/Eliminacion");

                    String tipo = read.nextLine();

                    System.out.println("Introduzca los participantes del torneo: ");

                    switch (tipo) {

                        case "Standard":

                            ArrayList<Coche> participantesS = new ArrayList<Coche>();
                            try {
                                participantesS = anhadirParticipantes();
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("*ERROR*\nNO HAY NINGÚN COCHE REGISTRADO O SON INSUFICIENTES");
                            }

                            if (participantesS.size() >= 3) {

                                Carrera carreraS = new Carrera(participantesS, nombreTorneo,
                                        Carrera.TipoCarrera.TorneoStandard);

                                carreras.add(carreraS);

                                System.out.println("Torneo Standard " + nombreTorneo + " creado !");
                                marca = true;

                            } else {
                                System.out.println("*ERROR*\nTIENE QUE HABER AL MENOS 3 PARTICIPANTES EN EL TORNEO");
                            }

                            break;

                        case "Eliminacion":

                            ArrayList<Coche> participantesE = new ArrayList<Coche>();
                            try {
                                participantesE = anhadirParticipantes();
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("*ERROR*\nNO HAY NINGÚN COCHE REGISTRADO O SON INSUFICIENTES");
                            }

                            if (participantesE.size() >= 3) {

                                Carrera carreraE = new Carrera(participantesE, nombreTorneo,
                                        Carrera.TipoCarrera.TorneoEliminacion);

                                carreras.add(carreraE);

                                System.out.println("Torneo Eliminacion " + nombreTorneo + " creado !");
                                marca = true;

                            } else {

                                System.out.println("*ERROR*\nTIENE QUE HABER AL MENOS 3 PARTICIPANTES EN EL TORNEO");
                            }

                            break;

                        default:
                            System.out.println("*ERROR*\nINTRODUCE UN TIPO DE TORNEO VÁLIDO (Standard/Eliminacion)");

                    }

                } else {

                    System.out.println("*ERROR*\n Nombre de carrera ya existente");
                }

            } else {
                System.out.println("*ERROR*\nEl nombre del torneo no puede estar en blanco");
            }

        } while ((nombreTorneo.equals("") == true) || ((buscarTorneo(nombreTorneo) == true) && (!marca)));

        System.out.println("--------------------------------------------");

    }

    // Para iniciar un torneo
    public static void iniciarTorneo() {

        System.out.println("--------------------------------------------\nIntroduce el nombre del Torneo que se va a iniciar: ");
        String premio = read.nextLine();

        if (buscarTorneo(premio)) {

            for (Carrera c : carreras) {

                if (premio.equals(c.getNombrePremio())) {

                    c.ejecutarTorneo();

                    break;

                }

            }

        } else {

            System.out.println("*ERROR*\nEL NOMBRE DEL PREMIO INTRODUCIDO NO EXISTE");

        }

        System.out.println("--------------------------------------------");

    }

    // Para mostrar los torneos creados en el array carreras
    public static void mostrarTorneos() {

        System.out.println("Mostrando torneos creados....");

        for (Carrera c : carreras) {

            if ((c.getTipo() == Carrera.TipoCarrera.TorneoStandard)
                    || (c.getTipo() == Carrera.TipoCarrera.TorneoEliminacion)) {

                System.out.println(c);

            }

        }

    }

    // *********************************************************************************************
    // FUNCIONES RELATIVAS A FICHEROS: 
    //ESCRITURAS
    public static void guardarCoches() throws Exception {

        JSONObject objC = new JSONObject();
        JSONArray arrayC = new JSONArray();

        for (Coche c : coches) {

            JSONArray arrayC2 = new JSONArray();

            arrayC2.add(c.getId());
            arrayC2.add(c.getMarca());
            arrayC2.add(c.getModelo());
            arrayC2.add(c.getEscuderia());

            arrayC.add(arrayC2);

        }

        objC.put("coches", arrayC);

        Files.write(Paths.get("ficheroCoches"), arrayC.toJSONString().getBytes());

    }

    public static void guardarCarreras() throws Exception {

        JSONObject objC = new JSONObject();
        JSONArray arrayC = new JSONArray();

        for (Carrera c : carreras) {
            JSONArray arrayC2 = new JSONArray();
            JSONArray arrayP = new JSONArray();

            for (Coche co : c.getParticipantes()) {
                JSONArray arrayCoche = new JSONArray();

                arrayCoche.add(co.getId());
                arrayCoche.add(co.getMarca());
                arrayCoche.add(co.getModelo());
                arrayCoche.add(co.getEscuderia());

                arrayP.add(arrayCoche);

            }

            arrayC2.add(arrayP);
            arrayC2.add(c.getNombrePremio());
            //Pasar el Enum a String para que lo escriba bien en el JSON
            arrayC2.add(c.getTipo().toString());

            arrayC.add(arrayC2);

        }

        objC.put("carreras", arrayC);
        Files.write(Paths.get("ficheroCarreras"), arrayC.toJSONString().getBytes());

    }

    //LECTURAS 
    public static void cargarCoches() throws IOException, ParseException {

        FileReader reader = new FileReader("ficheroCoches");
        JSONParser jsonParser = new JSONParser();
        //Array principal
        JSONArray ja = new JSONArray();

        ja = (JSONArray) jsonParser.parse(reader);
        //System.out.println(ja);

        for (int i = 0; i < ja.size(); i++) {

            //Array que contendrá cada coche
            JSONArray ja2 = new JSONArray();
            ja2 = (JSONArray) ja.get(i);

            //Creamos coches y los metemos en el array coches de Control
            Coche coche = new Coche();

            coche.setId((String) ja2.get(0));
            coche.setMarca((String) ja2.get(1));
            coche.setModelo((String) ja2.get(2));
            coche.setEscuderia((String) ja2.get(3));

            coches.add(coche);

        }

        System.out.println("COCHES REGISTRADOS: ");

        for (Coche c : coches) {

            System.out.println(c);

        }

    }

    public static void cargarCarreras() throws IOException, ParseException {

        FileReader reader = new FileReader("ficheroCarreras");
        JSONParser jsonParser = new JSONParser();

        JSONArray jaCarreras = new JSONArray();

        //Array principal
        jaCarreras = (JSONArray) jsonParser.parse(reader);
        //System.out.println(jaCarreras);

        for (int i = 0; i < jaCarreras.size(); i++) {

            //Array que contendrá cada carrera
            JSONArray jaCarrera = new JSONArray();
            jaCarrera = (JSONArray) jaCarreras.get(i);

            //Array que contendrá los participantes (coches) de una carrera
            JSONArray jaParticip = new JSONArray();
            jaParticip = (JSONArray) jaCarrera.get(0);
            //Array para ir almacenando los coches de una carrera
            ArrayList<Coche> participantesFichero = new ArrayList<Coche>();

            //Recorremos el array de participantes
            for (int j = 0; j < jaParticip.size(); j++) {

                Coche coche = new Coche();

                //El array de participantes, a su vez, tiene coches almacenados en otro array
                JSONArray jaCoche = new JSONArray();
                jaCoche = (JSONArray) jaParticip.get(j);

                coche.setId((String) jaCoche.get(0));
                coche.setMarca((String) jaCoche.get(1));
                coche.setModelo((String) jaCoche.get(2));
                coche.setEscuderia((String) jaCoche.get(3));

                //Metemos el coche en el array de coches anterior
                participantesFichero.add(coche);
            }

            //SEGUIMOS
            String nombrePremio = (String) jaCarrera.get(1);

            String tipo = (String) jaCarrera.get(2);
            Carrera.TipoCarrera tipoC = null;

            switch (tipo) {

                case "Standard":
                    tipoC = Carrera.TipoCarrera.Standard;

                    break;

                case "Eliminacion":
                    tipoC = Carrera.TipoCarrera.Eliminacion;

                    break;

                case "TorneoStandard":
                    tipoC = Carrera.TipoCarrera.TorneoStandard;

                    break;

                case "TorneoEliminacion":
                    tipoC = Carrera.TipoCarrera.TorneoEliminacion;

                    break;

                default:
                    System.out.println("*ERROR*\nTIPO DE CARRERA INCORRECTO");

            }

            //Creamos la carrera y la añadimos al array de Control
            Carrera carrera = new Carrera(participantesFichero, nombrePremio, tipoC);

            carreras.add(carrera);

        }

        //Mostramos todas las carreras del array de Control
        System.out.println("CARRERAS REGISTRADAS: ");
        for (Carrera c : carreras) {

            System.out.println(c);

        }

    }

}
