package main;

import main.CRUD.CRUD;

public class Main {

    public static void main(String[] args) {
        
        // 1. Instanciamos la clase CRUD una sola vez para usarla en ambas pruebas
        CRUD misConsultas = new CRUD();
        
        // ========================================================
        // 🛑 3. PRUEBA: BORRADO LÓGICO (desactivarUsuario)
        // ========================================================
        System.out.println("=== INICIANDO PRUEBA DE DESACTIVACIÓN (Borrado Lógico) ===");
        
        // NOTA: Pon un ID real de tu tabla que quieras pasar a estado 'inactivo'
        int idADesactivar = 1; 
        
        System.out.println("Cambiando el estado a 'inactivo' para el ID: " + idADesactivar);
        misConsultas.desactivarUsuario(idADesactivar);
        
        System.out.println("=== FIN DE LA PRUEBA DE DESACTIVACIÓN ===\n");


        // ========================================================
        // ❌ 4. PRUEBA: BORRADO FÍSICO (eliminarUsuario)
        // ========================================================
        System.out.println("=== INICIANDO PRUEBA DE ELIMINACIÓN (Borrado Físico) ===");
        
        // ADVERTENCIA: Este método usa un DELETE, lo que significa que borrará la fila por completo.
        // Usa un ID de algún usuario de pruebas que no te importe perder (por ejemplo, el ID 6).
        int idAEliminar = 1; 
        
        System.out.println("Borrando permanentemente de la base de datos el ID: " + idAEliminar);
        misConsultas.eliminarUsuario(idAEliminar);
        
        System.out.println("=== FIN DE LA PRUEBA DE ELIMINACIÓN ===");
    }
}