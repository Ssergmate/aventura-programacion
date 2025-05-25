package controller;

import model.Jugador;
import model.Granjero;
import model.Minero;
import model.Ligon;
import model.Enemigo;
import model.Escenario;
import model.Ataque;
import model.Objeto;
import service.UsuarioService;
import service.BatallaService;
import repository.DatabaseConnection;
import dto.LoginRequestDTO;
import dto.RegistroRequestDTO;

import java.util.Scanner;
import java.util.List;

public class Inicio {

    private static Scanner sc = new Scanner(System.in);
    private static UsuarioService usuarioService = new UsuarioService();
    private static BatallaService batallaService = new BatallaService(sc);

    public static void main(String[] args) {
        // Asegúrate de crear las tablas de la BD al inicio
        DatabaseConnection.createTables();

        System.out.println("Bienvenido al trabajo de Sergio, Pablo y Francis");

        // Bucle principal para el menú de login/registro
        boolean loggedIn = false;
        Jugador jugadorActual = null;

        while (!loggedIn) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Ver Top Jugadores");
            System.out.println("4. Salir del Juego");
            System.out.print("Elige una opción: ");

            int opcion = sc.nextInt();
            sc.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    jugadorActual = handleLogin();
                    if (jugadorActual != null) {
                        loggedIn = true;
                    }
                    break;
                case 2:
                    handleRegistro();
                    break;
                case 3:
                    mostrarTopJugadores();
                    break;
                case 4:
                    System.out.println("¡Gracias por jugar! Adiós.");
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Opción inválida. Intenta de nuevo.");
            }
        }

        // Si el login es exitoso, continúa con el flujo del juego
        System.out.println("Contraseña correcta. Bienvenido a...");
        System.out.println(
                " SSSSSSS    TTTTTTTTT    AAAAAAA    RRRRRRRR    DDDDDDD      EEEEEEEEE    WW         WW         WW        VV        VV    AAAAAA        LL        LL         EEEEEEEEE YY      YY\r\n"
                        + "SS          TT      AA        AA RR        DD        DD EE          WW         WW WW         WW          VV      VV   AA        AA LL        LL         EE          YY    YY\r\n"
                        + " SSSSSS      TT      AAAAAAAAA    RRRRRRRR    DD        DD EEEEEEEE     WW      WW    WW      WW            VV    VV    AAAAAAAAAA  LL        LL         EEEEEEEE      YY YY      \r\n"
                        + "   SS        TT      AA        AA RR      RR    DD        DD EE            WW WW       WW WW             VV VV        AA        AA  LL        LL         EE              YY      \r\n"
                        + "SSSSSSS      TT      AA        AA RR        RR DDDDDDD      EEEEEEEEE        WW         WW            VV        AA        AA  LLLLLLLL LLLLLLLL EEEEEEEEE      YY      \r\n"
                        + "");

        System.out.println("¡Bienvenido, " + jugadorActual.getNombreUsuario() + "!");
        System.out.println("Has elegido ser un " + jugadorActual.getNombre() + ".");
        System.out.println("Tu puntuación actual es: " + jugadorActual.getPuntuacionTotal());

        // Es crucial que 'jugadorActual' se cargue como su subclase específica para acceder a sus métodos.
        // Aquí asumimos que UsuarioService puede cargar el tipo concreto del jugador.
        Jugador jugadorConcreto = usuarioService.buscarJugadorPorNombreUsuarioConcreto(jugadorActual.getNombreUsuario());

        if (jugadorConcreto != null) {
            switch (jugadorConcreto.getNombre().toLowerCase()) {
                case "granjero":
                    iniciarAventuraGranjero((Granjero) jugadorConcreto);
                    break;
                case "minero":
                    iniciarAventuraMinero((Minero) jugadorConcreto);
                    break;
                case "ligon":
                    iniciarAventuraLigon((Ligon) jugadorConcreto);
                    break;
                default:
                    System.out.println("Tipo de personaje desconocido. No se puede iniciar la aventura.");
                    break;
            }
        } else {
            System.out.println("Error al cargar la información completa del jugador. Saliendo.");
            System.exit(1);
        }

        sc.close();
    }

    // --- Métodos de manejo de usuario (Login/Registro/Top) ---
    private static Jugador handleLogin() {
        System.out.print("Introduce tu nombre de usuario: ");
        String username = sc.nextLine();
        System.out.print("Introduce tu contraseña: ");
        String password = sc.nextLine();

        // Usar DTO para el login
        LoginRequestDTO loginRequest = new LoginRequestDTO(username, password);
        Jugador loggedInJugador = usuarioService.login(loginRequest);

        return loggedInJugador;
    }

    private static void handleRegistro() {
        System.out.print("Introduce un nuevo nombre de usuario: ");
        String username = sc.nextLine();
        System.out.print("Introduce una contraseña: ");
        String password = sc.nextLine();
        System.out.print("Confirma la contraseña: ");
        String confirmPassword = sc.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("Las contraseñas no coinciden. Por favor, intenta de nuevo.");
            return;
        }

        String personajeElegido = null;
        int eleccionPersonaje;
        do {
            System.out.println("Elige tu personaje inicial:");
            System.out.println("1: Granjero");
            System.out.println("2: Minero");
            System.out.println("3: Ligón");
            System.out.print("Tu elección: ");
            eleccionPersonaje = sc.nextInt();
            sc.nextLine();

            switch (eleccionPersonaje) {
                case 1:
                    personajeElegido = "Granjero";
                    break;
                case 2:
                    personajeElegido = "Minero";
                    break;
                case 3:
                    personajeElegido = "Ligon";
                    break;
                default:
                    System.out.println("Opción de personaje inválida.");
            }
        } while (personajeElegido == null);

        // Usar DTO para el registro
        RegistroRequestDTO registroRequest = new RegistroRequestDTO(username, password, confirmPassword, personajeElegido);

        if (usuarioService.registrarUsuario(registroRequest)) {
            System.out.println("Registro completado. ¡Ahora puedes iniciar sesión!");
        } else {
            System.out.println("Error en el registro. Intenta de nuevo.");
        }
    }

    private static void mostrarTopJugadores() {
        System.out.println("\n--- TOP 5 JUGADORES ---");
        List<Jugador> topPlayers = usuarioService.getTopPlayers(5); // Llama al método del servicio
        if (topPlayers.isEmpty()) {
            System.out.println("No hay jugadores registrados todavía.");
        } else {
            for (int i = 0; i < topPlayers.size(); i++) {
                Jugador j = topPlayers.get(i);
                // Mostrar solo nombre de usuario y puntuación total del jugador
                System.out.println((i + 1) + ". " + j.getNombreUsuario() + " (" + j.getNombre() + ") - Puntuación: " + j.getPuntuacionTotal());
            }
        }
    }

    // --- Función para curar al jugador ---
    private static void curar(Jugador jugador) {
        int costoCuracion = 30; // Costo de ejemplo para curarse
        int vidaRecuperada = 50; // Vida recuperada de ejemplo

        if (jugador.getVida() < jugador.getVidaMaxima()) {
            System.out.println("¿Quieres curarte? Te costará $" + costoCuracion + " y recuperarás " + vidaRecuperada + " de vida.");
            System.out.println("1. Sí");
            System.out.println("2. No");
            int opcion = sc.nextInt();
            sc.nextLine();

            if (opcion == 1) {
                if (jugador.getPuntos() >= costoCuracion) {
                    jugador.setPuntos(jugador.getPuntos() - costoCuracion);
                    jugador.recibirDano(-vidaRecuperada); // Daño negativo para curar
                    // Asegurarse de que la vida no exceda la vida máxima
                    if (jugador.getVida() > jugador.getVidaMaxima()) {
                        jugador.setVida(jugador.getVidaMaxima());
                    }
                    System.out.println("Te has curado. Vida actual: " + jugador.getVida() + ". Dinero actual: $" + jugador.getPuntos());
                    usuarioService.actualizarJugador(jugador); // Actualizar en BD
                } else {
                    System.out.println("No tienes suficiente dinero para curarte.");
                }
            } else {
                System.out.println("Decides no curarte por ahora.");
            }
        } else {
            System.out.println("Tu vida ya está al máximo.");
        }
    }

    // --- Lógica de la Tienda (podría ser una clase/servicio aparte) ---
    private static void Tienda(String[] productos, Jugador jugador) {
        System.out.println("\n--- Tienda ---");
        System.out.println("Dinero actual: $" + jugador.getPuntos());
        int opcion;
        do {
            System.out.println("¿Qué quieres comprar?");
            for (String producto : productos) {
                System.out.println(producto);
            }
            System.out.print("Elige un número: ");
            opcion = sc.nextInt();
            sc.nextLine(); // Consumir salto de línea

            // Opción de curarse
            if (opcion == (productos.length - 1) && productos[opcion -1].contains("Curarse")) { // Asumiendo que "Curarse" es la penúltima opción
                curar(jugador);
            }
            else if (opcion >= 1 && opcion <= productos.length - 1) { // Excluir la opción "Salir" y "Curarse"
                String productoSeleccionado = productos[opcion - 1].split("\\.")[1].trim(); // Obtener nombre del producto
                String nombreObjeto = productoSeleccionado.split(" ")[0]; // Obtener el nombre (ej. "Trigo")
                int costo = Integer.parseInt(productoSeleccionado.substring(productoSeleccionado.indexOf("$") + 1, productoSeleccionado.indexOf(")"))
                        .trim());

                if (jugador.getPuntos() >= costo) {
                    Objeto nuevoObjeto = new Objeto(opcion, nombreObjeto, "Descripción de " + nombreObjeto, costo, "Tipo Genérico"); // ID, Nombre, Desc, Precio, Tipo
                    jugador.agregarObjeto(nuevoObjeto);
                    jugador.setPuntos(jugador.getPuntos() - costo);
                    System.out.println("Has comprado " + nombreObjeto + ". Dinero restante: $" + jugador.getPuntos());
                } else {
                    System.out.println("No tienes suficiente dinero para comprar " + nombreObjeto + ".");
                }
            } else if (opcion != productos.length) { // No es "Salir" y es una opción inválida
                System.out.println("Opción inválida.");
            }
        } while (opcion != productos.length);
        System.out.println("Saliendo de la tienda.");
    }

    // --- Aventura de Granjero ---
    private static void iniciarAventuraGranjero(Granjero granjero) {
        System.out.println("\n--- Aventura de Granjero ---");
        System.out.println("Como granjero deberás cultivar y ganar todo el dinero que puedas para conseguir la meta de dinero en menos de dos días, o en su defecto completar el centro cívico con las colecciones o mudarte a la ciudad con el billete, solo así podrás ganar el juego");
        System.out.println("Si mueres, pierdes.");
        System.out.println("Estás en primavera, el trigo es el cultivo principal. Debes comprarlo y plantarlo.");

        String[] productos = { "1. Trigo ($50)", "2. Zanahoria ($50)", "3. Algodón ($50)",
                "4. Colección de piedras ($50)", "5. Colección de plantas ($50)", "6. Colección de pescados ($50)",
                "7. Anillo de boda ($50)", "8. Ramo de flores ($50)", "9. Billete de ida a la ciudad ($50)",
                "10. Curarse ($30)", // Opción de curarse
                "11. Salir" };

        Tienda(productos, granjero);

        boolean tieneTrigo = granjero.getInventario().stream().anyMatch(o -> o.getNombre().equalsIgnoreCase("Trigo"));

        if (!tieneTrigo) {
            System.out.println("Se te olvidó comprar el trigo, no pasa nada esta vez invito yo por ser tu primer día, no queremos que te estanques el primer día, te lo descuento de la cuenta");
            Objeto trigoGratis = new Objeto(1, "Trigo", "Semillas de trigo", 50, "Cultivo");
            granjero.agregarObjeto(trigoGratis);
            granjero.setPuntos(granjero.getPuntos() - 50);
            System.out.println("Dinero actual: " + granjero.getPuntos());
        }

        System.out.println("Ahora es hora de plantar, sin embargo hay un problema, tu vecino te cogió la azada prestada, deberás ir a convencerle de que te la devuelva");
        System.out.println("Te diriges a casa de tu vecino tocándole la puerta y este abría la puerta al rato");
        System.out.println("Hola vecino ¿Qué deseas🧑‍🦳?");
        System.out.println("1: Disculpa vecino, ¿Tienes mi azada todavía?");
        System.out.println("2: Como no me devuelvas la azada te prendo fuego toda la casa");
        System.out.println("3: No, nada (me gastaré 50 en una azada nueva)");
        int opcion = sc.nextInt();
        sc.nextLine();
        switch (opcion) {
            case 1:
                System.out.println("Si claro vecino, aquí la tienes... recibes tu azada de vuelta");
                granjero.setTieneAzada(true);
                break;
            case 2:
                System.out.println("*El vecino cabreado te devolvería la azada en la cabeza bajándote la vida 25 puntos de vida, te queda " + (granjero.getVida() - 25));
                granjero.recibirDano(25);
                granjero.setTieneAzada(true);
                break;
            case 3:
                System.out.println("Está bien, hasta otra vecino pierdes 50 en comprarte una azada nueva");
                granjero.setPuntos(granjero.getPuntos() - 50);
                granjero.setTieneAzada(true);
                System.out.println("Dinero actual: " + granjero.getPuntos());
                break;
        }

        System.out.println("Ahora que vuelves a tener azada puedes empezar a cultivar, aquí tienes las instrucciones:");
        System.out.println("- Primer paso arar la tierra");
        System.out.println("- Segundo paso plantar las semillas");
        System.out.println("- Tercer paso regar la tierra");
        System.out.println("Es importante aclarar que cualquier opción que no sea en este orden estropearán el cultivo");
        System.out.println("¿Entendido? ¡Pues a plantar!");
        System.out.println("1: Plantar semillas");
        System.out.println("2: Regar la tierra");
        System.out.println("3: Arar la tierra");
        opcion = sc.nextInt();
        sc.nextLine();
        if (opcion == 3) {
            System.out.println("*Aras la tierra en el orden correcto");
            System.out.println("1: Plantar semillas");
            System.out.println("2: Regar la tierra");
            System.out.println("3: Arar la tierra");
            opcion = sc.nextInt();
            sc.nextLine();
            if (opcion == 1) {
                System.out.println("*Después de arar colocas cuidadosamente las semillas de trigo");
                System.out.println("1: Plantar semillas");
                System.out.println("2: Regar la tierra");
                System.out.println("3: Arar la tierra");
                opcion = sc.nextInt();
                sc.nextLine();
                if (opcion == 2) {
                    System.out.println("Terminas de plantar el trigo al regarlo, ganando $100");
                    granjero.setPuntos(granjero.getPuntos() + 100);
                } else {
                    System.out.println("Estropeaste el cultivo, no ganas dinero");
                }
            } else {
                System.out.println("Estropeaste el cultivo, no ganas dinero");
            }
        } else {
            System.out.println("Estropeaste el cultivo, no ganas dinero");
        }
        System.out.println("Dinero actual: " + granjero.getPuntos());

        System.out.println("Te alejas de los cultivos viendo como en tu buzón había algo, vas a revisar y empiezas a leer la carta de tu madre");
        System.out.println("\"Nunca soy, pero siempre seré. Nadie me ha visto jamás, pero todos saben que existo. ¿Qué soy?\"");
        System.out.println("1. El orden");
        System.out.println("2. El mañana");
        System.out.println("3. El silencio");
        opcion = sc.nextInt();
        sc.nextLine();
        if (opcion == 2) {
            System.out.println("Acertaste el acertijo ganándote $50");
            granjero.setPuntos(granjero.getPuntos() + 50);
        } else {
            System.out.println("Fallas el acertijo perdiendo el dinero de tu paga");
        }
        System.out.println("Dinero actual: " + granjero.getPuntos());

        System.out.println("Tras mandarle la carta de respuesta a tu madre te vas a dormir tras ese día largo, metiéndote en la cama listo para contar ovejitas");
        System.out.println("*Una ovejita más otra ovejita son...");
        opcion = sc.nextInt();
        sc.nextLine();
        if (opcion == 2) {
            System.out.println(" y ∫ \\r\\n\" + \"1+ln(x)\\r\\n\" + \"xsin(x)\\r\\n\" + \"\\u200B\\r\\n\" + \" dx ¿Cuántas ovejitas son?");
            System.out.println("1. 10");
            System.out.println("2. 37");
            System.out.println("3. 5");
            opcion = sc.nextInt();
            sc.nextLine();
            if (opcion == 1) {
                System.out.println("*Consigues dormirte con éxito");
            } else {
                System.out.println("Eran 10 ovejitas, duermes mal por lo que se te baja la vida");
                granjero.recibirDano(20);
            }
        } else {
            System.out.println("Eran 2 ovejitas, duermes mal por lo que se te baja la vida");
            granjero.recibirDano(20);
        }
        System.out.println("Vida actual: " + granjero.getVida());

        // Actualizar jugador en la BD después de eventos
        usuarioService.actualizarJugador(granjero);


        System.out.println("*Pasa el día y vas a la tienda a comprar, como ya está empezando verano comprar zanahorias es lo que más dinero te daría");
        Tienda(productos, granjero); // Otra visita a la tienda
        System.out.println("Dinero actual: $" + granjero.getPuntos());

        System.out.println("Al volver a tu campo a plantar te encuentras con unos pájaros que están estropeando tus cultivos, decides armarte e ir a combatirlos");

        // --- PREPARAR EL ENEMIGO Y ESCENARIO PARA LA BATALLA ---
        Enemigo pajaro = new Enemigo("Pájaro Malvado", 100, "Aviar");
        pajaro.agregarAtaque(new Ataque(1, "Picotazo", 10, "total"));
        pajaro.agregarAtaque(new Ataque(2, "Aletazo", 20, "total"));
        pajaro.agregarAtaque(new Ataque(3, "Cagado Sónica", 30, "total"));
        Escenario campo = new Escenario(1, "Campo Abierto", "Despejado", "Mañana", "Llano");

        // Llamar a BatallaService
        boolean batallaGanada = batallaService.iniciarBatalla(granjero, pajaro, campo);

        if (!batallaGanada) {
            System.out.println("Has muerto fin del juego");
            usuarioService.actualizarJugador(granjero); // Guardar estado final de vida
            System.exit(0);
        } else {
            // Lógica del juego si el protagonista sobrevive a la batalla
            System.out.println("Tu vida actual: " + granjero.getVida());

            int finals = 0;
            do {
                System.out.println("Tras la ardua pelea decides tomarte un descanso de la plantación, pues tu campo había sido arrollado por la épica pelea entre el mamífero y el ovíparo, por lo que sigues trabajando en tus otros planes como sería modificar tu tractor");
                System.out.println("Vas al corral donde lo tienes y empiezas a modificarlo a tu gusto");
                System.out.println("¿Qué color le pones al tractor?");
                System.out.println("1. Rojo");
                System.out.println("2. Verde");
                System.out.println("3. Amarillo");
                String Colortractor = null;
                String ruedastractor = null;

                opcion = sc.nextInt();
                sc.nextLine();
                if (opcion == 1) {
                    System.out.println("Pintas el camión de color rojo");
                    Colortractor = "rojo";
                } else if (opcion == 2) {
                    System.out.println("Pintas el camión de color verde");
                    Colortractor = "verde";
                } else if (opcion == 3) {
                    System.out.println("YO TENGO UN TRACTOR AMARILLOOOO");
                    Colortractor = "amarillo";
                } else {
                    System.out.println("Eso no es un color válido. Color predeterminado: Gris.");
                    Colortractor = "gris";
                }

                System.out.println("¿Qué ruedas quieres para tu tractor?");
                System.out.println("1. Ruedas grandes");
                System.out.println("2. Ruedas pequeñas");

                opcion = sc.nextInt();
                sc.nextLine();
                if (opcion == 1) {
                    System.out.println("Le colocas ruedas grandes a tu tractor");
                    ruedastractor = "grandes";
                } else if (opcion == 2) {
                    System.out.println("Le colocas ruedas pequeñas a tu tractor");
                    ruedastractor = "pequeñas";
                } else {
                    System.out.println("Eso no es un tamaño válido. Ruedas predeterminadas: Medianas.");
                    ruedastractor = "medianas";
                }

                System.out.println("*Terminas de modificar tu tractor, observando el precioso tractor de color "
                        + Colortractor + " con sus ruedas " + ruedastractor);
                System.out.println(
                        "Por último decides pasarte una vez más por la tienda para ver cuántos ahorros tienes o si puedes comprarte algo chulo");
                System.out.println("*Si alcanzaste $150 al salir de la tienda ganarás con el final financiero");

                Tienda(productos, granjero);
                System.out.println("Dinero actual: $" + granjero.getPuntos());

                // Actualizar jugador en la BD después de eventos
                usuarioService.actualizarJugador(granjero);


                boolean tieneColeccionPiedras = granjero.getInventario().stream().anyMatch(o -> o.getNombre().equalsIgnoreCase("Colección de piedras"));
                boolean tieneColeccionPlantas = granjero.getInventario().stream().anyMatch(o -> o.getNombre().equalsIgnoreCase("Colección de plantas"));
                boolean tieneColeccionPescados = granjero.getInventario().stream().anyMatch(o -> o.getNombre().equalsIgnoreCase("Colección de pescados"));
                boolean tieneBilleteCiudad = granjero.getInventario().stream().anyMatch(o -> o.getNombre().equalsIgnoreCase("Billete de ida a la ciudad"));


                if (granjero.getPuntos() >= 150) {
                    finals = 1;
                } else if (tieneColeccionPiedras && tieneColeccionPlantas && tieneColeccionPescados) {
                    finals = 2;
                } else if (tieneBilleteCiudad) {
                    finals = 3;
                } else if (granjero.getVida() <= 0) { // Si muere en la batalla final, se cuenta como final
                    finals = 4; // Final de muerte
                }
                else {
                    finals = 0;
                }

            } while (finals == 0);

            if (finals == 1) {
                System.out.println("¡Felicidades! Llegaste a la meta de dinero en estos dos días, ¡tú ganas!");
            } else if (finals == 2) {
                System.out.println("¡Con todos tus esfuerzos y dinero consigues comprar todas las colecciones del centro cívico, completándolo junto al juego, tú ganas!");
            } else if (finals == 3) {
                System.out.println("¡Tras comprar el billete a la ciudad te mudarías de inmediato, viviendo junto con tu madre, ganas!");
            } else if (finals == 4) {
                System.out.println("Falleces tras el combate, pierdes.");
            } else {
                System.out.println("No conseguiste llegar a la meta de dinero ni cumplir otros objetivos, pierdes.");
            }
        }
    }


    private static void iniciarAventuraMinero(Minero minero) {
        System.out.println("\n--- Aventura de Minero ---");
        System.out.println("Como minero deberás excavar y recolectar recursos valiosos. Para ganar deberás obtener piedra luminosa, llegar al final del nivel 2 de la mina o salir con una pepita de oro.");

        Enemigo rataMina = new Enemigo("Rata de Mina", 100, "Roedor");
        rataMina.agregarAtaque(new Ataque(1, "Mordisco", 15, "total"));
        rataMina.agregarAtaque(new Ataque(2, "Cavar para sorprender", 25, "total"));
        rataMina.agregarAtaque(new Ataque(3, "Arañar", 35, "total"));
        Escenario minaNivel1 = new Escenario(1, "Mina Nivel 1", "Oscuro", "Noche", "Rocoso");


        System.out.println("Regresas a tu mina y decides comenzar a excavar.");
        System.out.println("Excavas profundamente y te enfrentas a la rata que acecha el túnel.");
        boolean batallaGanada = batallaService.iniciarBatalla(minero, rataMina, minaNivel1);

        if (!batallaGanada) {
            System.out.println("Te han quitado toda la vida, pierdes.");
            usuarioService.actualizarJugador(minero);
            System.exit(0);
        }

        // Actualizar jugador en la BD después de eventos
        usuarioService.actualizarJugador(minero);


        System.out.println("Después de la batalla, decides continuar excavando en busca de tesoros.");
        System.out.println("Mientras excavas llegas al primer nivel, donde hay un vendedor un tanto misterioso");
        System.out.println("Vendedor misterioso 👤: Hola minero, ¿te gustaría tomar esta poción?, con ella podrás ver con más claridad");
        System.out.println("1. CLARO");
        System.out.println("2. NO GRACIAS");
        int contestar = sc.nextInt();
        sc.nextLine();
        while (contestar != 1 && contestar != 2) {
            System.out.println("Opción inválida, introduce 1 si quieres la poción o 2 si no la quieres.");
            contestar = sc.nextInt();
            sc.nextLine();
        }
        if (contestar == 1) {
            System.out.println("¡El vendedor te la ha jugado!");
            System.out.println("La poción ha hecho que te quedes inconsciente y te ha quitado $50 del monedero");
            minero.setPuntos(minero.getPuntos() - 50);
            System.out.println("Tu monedero tiene $" + minero.getPuntos());
        } else {
            System.out.println("Has sido astuto ya que la poción tendría algún efecto secundario");
        }

        // Actualizar jugador en la BD después de eventos
        usuarioService.actualizarJugador(minero);

        System.out.println("Te encuentras un esqueleto el cual era un antiguo minero");
        System.out.println("Tiene en su mano una nota la cual dice:");
        System.out.println("A quien lea esto, he escondido una pepita de oro en la mina. Recuerda:");
        System.out.println("La pepita de oro se encuentra en la caja con una cruz.");
        System.out.println("Recuerda esta dirección para más adelante");
        System.out.println("De repente te encuentras un murciélago y tienes que actuar rápido, ¿qué haces?");
        System.out.println("1. Esquivar");
        System.out.println("2. Cubrirte");
        int actua = sc.nextInt();
        sc.nextLine();
        while (actua != 1 && actua != 2) {
            System.out.println("Opción inválida, introduce 1 si quieres esquivar o 2 si quieres cubrirte");
            actua = sc.nextInt();
            sc.nextLine();
        }
        if (actua == 1) {
            System.out.println("¡Bien hecho!, el murciélago sigue su camino y no os habéis enfrentado");
        } else {
            System.out.println("El murciélago te ha mordido y te ha quitado 10 de vida.");
            minero.recibirDano(10);
            System.out.println("Tienes " + minero.getVida() + " de vida.");
            if (minero.getVida() <= 0) {
                System.out.println("Te han quitado toda la vida, pierdes");
                usuarioService.actualizarJugador(minero);
                System.exit(0);
            }
        }

        // Actualizar jugador en la BD después de eventos
        usuarioService.actualizarJugador(minero);

        System.out.println("Llegas al final del nivel 1 de la mina y te encuentras un 2x2 de cajas.");
        System.out.println("");
        System.out.println("—————————— ");
        System.out.println("| ❌ | ☠ |");
        System.out.println("|——————|");
        System.out.println("|  ⛏  | 💎 |");
        System.out.println("—————————— ");
        System.out.println("¿Qué caja eliges? (fila, columna - ej: 1,1)");
        System.out.print("Fila: ");
        int fila = sc.nextInt();
        System.out.print("Columna: ");
        int columna = sc.nextInt();
        sc.nextLine();

        if (fila == 1 && columna == 1) {
            System.out.println("Has elegido la caja con la cruz. ¡Encontraste una pepita de oro! ¡Ganas el juego!");
            minero.agregarObjeto(new Objeto(100, "Pepita de Oro", "Una brillante pepita de oro", 0, "Tesoro"));
            usuarioService.actualizarJugador(minero);
            System.exit(0); // El jugador gana
        } else {
            System.out.println("Esa caja estaba vacía o contenía un monstruo.");
            System.out.println("Un gólem de piedra aparece!");
            Enemigo golem = new Enemigo("Gólem de Piedra", 150, "Elemental");
            golem.agregarAtaque(new Ataque(1, "Puñetazo de Roca", 25, "total"));
            golem.agregarAtaque(new Ataque(2, "Terremoto", 40, "total"));
            boolean batallaGolemGanada = batallaService.iniciarBatalla(minero, golem, minaNivel1);

            if (!batallaGolemGanada) {
                System.out.println("El gólem te ha derrotado. Fin del juego.");
                usuarioService.actualizarJugador(minero);
                System.exit(0);
            } else {
                System.out.println("Derrotaste al gólem. Continúas tu aventura.");
                // Opción para ir al siguiente nivel o buscar otros tesoros
                System.out.println("Tienes la opción de seguir excavando al Nivel 2 o salir de la mina.");
                System.out.println("1. Ir al Nivel 2");
                System.out.println("2. Salir de la mina");
                int nextAction = sc.nextInt();
                sc.nextLine();

                if (nextAction == 1) {
                    System.out.println("Desciendes al Nivel 2 de la mina.");
                    // Continúa la aventura del Minero en el Nivel 2
                    iniciarAventuraMineroNivel2(minero);
                } else {
                    System.out.println("Decides salir de la mina por ahora.");
                    System.out.println("No has conseguido ninguna de las formas de ganar. Pierdes.");
                }
            }
        }
        usuarioService.actualizarJugador(minero); // Guarda el estado antes de salir/continuar
    }

    private static void iniciarAventuraMineroNivel2(Minero minero) {
        System.out.println("\n--- Mina Nivel 2 ---");
        System.out.println("Las profundidades de la mina son más peligrosas, pero también más recompensas.");

        // Nuevo enemigo para el Nivel 2
        Enemigo dragonCristal = new Enemigo("Dragón de Cristal", 200, "Mítico");
        dragonCristal.agregarAtaque(new Ataque(1, "Aliento de Cristal", 40, "total"));
        dragonCristal.agregarAtaque(new Ataque(2, "Garra Diamante", 55, "total"));
        Escenario minaNivel2 = new Escenario(2, "Mina Nivel 2", "Brillante", "Eterna Oscuridad", "Cueva de Cristales");

        System.out.println("Un Dragón de Cristal te bloquea el camino. ¡Prepárate para la batalla final!");
        boolean batallaGanada = batallaService.iniciarBatalla(minero, dragonCristal, minaNivel2);

        if (!batallaGanada) {
            System.out.println("El Dragón de Cristal te ha derrotado. Fin del juego.");
            usuarioService.actualizarJugador(minero);
            System.exit(0);
        } else {
            System.out.println("¡Has derrotado al Dragón de Cristal y alcanzado el final del Nivel 2! ¡Felicidades, ganas el juego!");
            usuarioService.actualizarJugador(minero);
        }
    }


    private static void iniciarAventuraLigon(Ligon ligon) {
        System.out.println("\n--- Aventura de Ligón ---");
        System.out.println("Como Ligón, tu objetivo es mejorar tu reputación e impresionar a los habitantes del pueblo. Para ganar, deberás conseguir un anillo y un ramo de flores, llegar a 150 puntos de reputación o casarte.");

        String[] productos = { "1. Trigo ($50)", "2. Zanahoria ($50)", "3. Algodón ($50)",
                "4. Colección de piedras ($50)", "5. Colección de plantas ($50)", "6. Colección de pescados ($50)",
                "7. Anillo de boda ($50)", "8. Ramo de flores ($50)", "9. Billete de ida a la ciudad ($50)",
                "10. Curarse ($30)", // Opción de curarse
                "11. Salir" };

        System.out.println("Empiezas tu día paseando por el pueblo. Ves a un grupo de personas hablando sobre el festival anual.");
        System.out.println("Decides acercarte y unirte a la conversación. ¿Qué haces?");
        System.out.println("1. Contar un chiste malo.");
        System.out.println("2. Escuchar atentamente y hacer preguntas relevantes.");
        System.out.println("3. Ignorarlos y pasar de largo.");
        int opcion = sc.nextInt();
        sc.nextLine();

        if (opcion == 2) {
            System.out.println("La gente te aprecia por tu interés. Ganas 10 puntos de reputación.");
            ligon.aumentarReputacion(10);
        } else if (opcion == 1) {
            System.out.println("Tu chiste cae como una bomba. Pierdes 5 puntos de reputación.");
            ligon.disminuirReputacion(5);
        } else {
            System.out.println("Pasas desapercibido. Tu reputación no cambia.");
        }
        System.out.println("Reputación actual: " + ligon.getReputacion());
        usuarioService.actualizarJugador(ligon);

        System.out.println("\nDecides ir a la tienda a ver qué objetos te pueden ayudar a mejorar tu reputación.");
        Tienda(productos, ligon);
        System.out.println("Dinero actual: $" + ligon.getPuntos());
        usuarioService.actualizarJugador(ligon);

        System.out.println("\nMientras caminas, te encuentras con un gato callejero. Parece hambriento.");
        System.out.println("1. Darle algo de tu comida (si tienes).");
        System.out.println("2. Ignorarlo.");
        System.out.println("3. Intentar espantarlo.");
        opcion = sc.nextInt();
        sc.nextLine();

        if (opcion == 1) {
            // Asumiendo que existe un objeto "Pan" en el inventario
            boolean tieneComida = ligon.getInventario().stream().anyMatch(o -> o.getNombre().equalsIgnoreCase("Pan"));
            if (tieneComida) {
                System.out.println("El gato come feliz. Una anciana que pasaba te ve y se impresiona. Ganas 15 puntos de reputación.");
                ligon.aumentarReputacion(15);
                // Si tienes un sistema de inventario más avanzado, aquí eliminarías el "Pan"
                // ligon.eliminarObjeto("Pan");
            } else {
                System.out.println("No tienes comida para darle al gato. La anciana te mira con decepción. Pierdes 5 puntos de reputación.");
                ligon.disminuirReputacion(5);
            }
        } else if (opcion == 3) {
            System.out.println("El gato te araña. Pierdes 10 de vida y 10 de reputación.");
            ligon.recibirDano(10);
            ligon.disminuirReputacion(10);
        } else {
            System.out.println("Ignoras al gato. Tu reputación no cambia.");
        }
        System.out.println("Reputación actual: " + ligon.getReputacion());
        System.out.println("Vida actual: " + ligon.getVida());
        usuarioService.actualizarJugador(ligon);

        // Comprobar condiciones de victoria para el Ligón
        boolean tieneAnillo = ligon.getInventario().stream().anyMatch(o -> o.getNombre().equalsIgnoreCase("Anillo de boda"));
        boolean tieneRamo = ligon.getInventario().stream().anyMatch(o -> o.getNombre().equalsIgnoreCase("Ramo de flores"));

        int ligonFinal = 0;
        // Asumimos que se necesita al menos 100 de reputación para casarse
        if (tieneAnillo && tieneRamo && ligon.getReputacion() >= 100) {
            ligonFinal = 1; // Casarse
        } else if (ligon.getReputacion() >= 150) {
            ligonFinal = 2; // Alta reputación
        } else if (ligon.getVida() <= 0) {
            ligonFinal = 3; // Muerte
        }

        switch (ligonFinal) {
            case 1:
                System.out.println("¡Con el anillo y el ramo, y tu alta reputación, le pides matrimonio a tu amado/a! ¡Felicidades, te has casado y ganas el juego!");
                break;
            case 2:
                System.out.println("¡Tu reputación es legendaria en el pueblo! Eres el más querido y ganas el juego por popularidad.");
                break;
            case 3:
                System.out.println("Tu vida ha llegado a cero. Falleces en la aventura de la popularidad. Pierdes.");
                break;
            default:
                System.out.println("No has logrado alcanzar ninguna de las metas del Ligón. Pierdes.");
                break;
        }
        usuarioService.actualizarJugador(ligon); // Guarda el estado final
    }
}