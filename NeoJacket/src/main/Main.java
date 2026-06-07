package main;

// IMPORTANTE: Importamos la clase CRUD desde su paquete correspondiente
import main.CRUD.CRUD;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBA DE CONEXIÓN Y REGISTRO ===");
        
        // 1. Instanciamos o creamos el objeto de la clase CRUD
        CRUD misConsultas = new CRUD();
        
        // 2. Inventamos datos de prueba para pasárselos al método
        String nombrePrueba = "Javier";
        String apellidoPrueba = "Andrei";
        String correoPrueba = "javier.test@neojacket.com"; // Asegúrate de que cambie en cada prueba por el UNIQUE
        String telefonoPrueba = "55551234";
        String fechaNacPrueba = "2005-10-25"; // Formato estricto AAAA-MM-DD
        String generoPrueba = "M";            // 'M', 'F' u 'Otro' según tu ENUM
        String passwordPrueba = "hash_secreto_123"; 
        String dpiPrueba = "1234567890101";   // 13 dígitos como String
        
        // 3. Ejecutamos el método
        System.out.println("Enviando datos a MySQL...");
        misConsultas.nuevoUsuario(
            nombrePrueba, 
            apellidoPrueba, 
            correoPrueba, 
            telefonoPrueba, 
            fechaNacPrueba, 
            generoPrueba, 
            passwordPrueba, 
            dpiPrueba
        );
        
        System.out.println("=== FIN DE LA PRUEBA ===");
    }
}