package View;

import Controller.GestionBD;
import Entity.Contacto;

import java.util.Scanner;

public class Menu {
    private static Scanner sc = new Scanner(System.in);
    public static void mostrarPrincipal(){
        int opcion = -1;
        while(opcion != 0){
            System.out.println("Bienvenido al gestor de contactos");
            System.out.println("1. Agregar contacto");
            System.out.println("2. Listar contactos");
            System.out.println("3. Buscar contacto por nombre");
            System.out.println("4. Editar contacto");
            System.out.println("5. Eliminar contacto");
            System.out.println("6. Filtrar contactos");
            System.out.println("7. Recuperar último eliminado");
            System.out.println("8. Eliminar todos los datos del programa");
            System.out.println("0. Salir");
            System.out.println("Ingrese una opción: ");
            opcion = Integer.parseInt(sc.nextLine());
            switch(opcion){
                case 1:
                    mostrarAgregar();
                    break;
                case 2:
                    mostrarListar();
                    break;
                case 3:
                    mostrarBuscar();
                    break;
                case 4:
                    mostrarEditar();
                    break;
                case 5:
                    mostrarEliminar();
                    break;
                case 6:
                    mostrarFiltrar();
                    break;
                case 7:
                    mostrarRecuperar();
                    break;
                case 8:
                    mostrarEliminarTodo();
                    break;
                case 0:
                    System.out.println("Hasta pronto...");
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
    }

    private static void mostrarEliminarTodo() {
        System.out.println("------------ Eliminar todos los datos del programa ------------");
        System.out.println("Desea eliminar todos los datos del programa? Esta operación no puede revertirse (S/N)");
        String respuesta = sc.nextLine();
        if(respuesta.equals("S")){
            // Llamo al controller
            GestionBD gestionBD = new GestionBD();
            gestionBD.eliminarTodosLosDatos();
        } else {
            System.out.println("No se eliminaron todos los datos del programa");
        }
    }

    private static void mostrarRecuperar() {
        System.out.println("------------ Recuperar último eliminado ------------");
        System.out.println("Desea recuperar el último contacto eliminado? (S/N)");
        String respuesta = sc.nextLine();
        if(respuesta.equals("S")){
            // Llamo al controller
            GestionBD gestionBD = new GestionBD();
            gestionBD.recuperarUltimoEliminado();
        } else {
            System.out.println("No se recuperó el último contacto eliminado");
        }
    }

    private static void mostrarFiltrar() {
        System.out.println("------------ Filtrar contactos ------------");
        System.out.println("Filtrar contactos cuyo nombre comiencen por la letra: ");
        char letra = sc.nextLine().charAt(0);

        // Llamo al controller
        GestionBD gestionBD = new GestionBD();
        gestionBD.filtrarContactosLetraNombre(letra);
    }

    private static void mostrarEliminar() {
        Contacto contacto = mostrarBuscar();
        if(contacto != null){
            System.out.println("¿Está seguro que desea eliminar el contacto? (S/N)");
            String respuesta = sc.nextLine();
            if(respuesta.equals("S")){
                // Llamo al controller
                GestionBD gestionBD = new GestionBD();
                gestionBD.eliminarContacto(contacto);
            }
        }
    }

    private static void mostrarEditar() {
        Contacto contacto = mostrarBuscar();
        if(contacto != null){
            System.out.println("------------ Editar contacto ------------");
            System.out.println("Nombre: " + contacto.getNombre());
            System.out.println("Ingrese el nuevo nombre: ");
            String nombre = sc.nextLine();
            System.out.println("Apellido: " + contacto.getApellido());
            System.out.println("Ingrese el nuevo apellido: ");
            String apellido = sc.nextLine();
            System.out.println("Teléfono: " + contacto.getTelefono());
            System.out.println("Ingrese el nuevo teléfono: ");
            int telefono = Integer.parseInt(sc.nextLine());
            System.out.println("Email: " + contacto.getEmail());
            System.out.println("EL EMAIL NO SE PUEDE MODIFICAR");

            // Llamo al controller
            GestionBD gestionBD = new GestionBD();
            contacto.setNombre(nombre);
            contacto.setApellido(apellido);
            contacto.setTelefono(telefono);
            gestionBD.editarContacto(contacto);
        }

    }

    private static Contacto mostrarBuscar() {
        System.out.println("------------ Buscar contacto ------------");
        System.out.println("Ingrese el nombre que desea buscar: ");
        String nombre = sc.nextLine();

        // Llamo al controller
        GestionBD gestionBD = new GestionBD();
        Contacto contacto = gestionBD.buscarContacto(nombre);
        if(contacto != null){
            System.out.println("Contacto encontrado");
            System.out.println("Nombre: " + contacto.getNombre());
            System.out.println("Apellido: " + contacto.getApellido());
            System.out.println("Teléfono: " + contacto.getTelefono());
            System.out.println("Email: " + contacto.getEmail());
            return contacto;
        } else {
            System.out.println("Contacto no encontrado");
        }
        return null;
    }

    private static void mostrarListar() {
        GestionBD gestionBD = new GestionBD();
        gestionBD.listarContactos();
    }

    public static void mostrarAgregar(){
        System.out.println("------------ Agregar contacto ------------");
        System.out.println("Ingrese el nombre: ");
        String nombre = sc.nextLine();
        System.out.println("Ingrese el apellido: ");
        String apellido = sc.nextLine();
        System.out.println("Ingrese el teléfono: ");
        int telefono = Integer.parseInt(sc.nextLine());
        System.out.println("Ingrese el email: ");
        String email = sc.nextLine();

        // Llamo al controller
        GestionBD gestionBD = new GestionBD();
        Contacto contacto = new Contacto(nombre, apellido, telefono, email);
        gestionBD.agregarContacto(contacto);
    }
}
