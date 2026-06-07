package main;

import main.CRUD.CRUD;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBA DE EDICIÓN ===");
        
        // 1. Instanciamos la clase CRUD
        CRUD misConsultas = new CRUD();
        
        // 2. Definimos las variables con los nuevos datos que queremos actualizar
        // IMPORTANTE: El ID debe ser uno que ya exista en tu base de datos (por ejemplo, el 1)
        int idAEditar = 1; 
        
        String nuevoNombre = "Javier Modificado";
        String nuevoApellido = "Andrei";
        String nuevoCorreo = "javier.nuevo@neojacket.com"; // Si cambias el correo, asegúrate que no exista en otro usuario
        String nuevoTelefono = "44449876";                // Teléfono editado
        String nuevoGenero = "M";
        String nuevoEstado = "suspendido";                 // Cambiamos el estado para probar el ENUM
        
        // 3. Ejecutamos el método de editar
        System.out.println("Enviando actualización a MySQL para el ID: " + idAEditar);
        
        misConsultas.editarUsuario(
            idAEditar, 
            nuevoNombre, 
            nuevoApellido, 
            nuevoCorreo, 
            nuevoTelefono, 
            nuevoGenero, 
            nuevoEstado
        );
        
        System.out.println("=== FIN DE LA PRUEBA ===");
    }
}