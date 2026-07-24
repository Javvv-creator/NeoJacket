package funcionalidades;

import java.io.ByteArrayInputStream;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;

public class ImpresoraComprobante {

    public static void imprimir(String texto, String nombreImpresora) throws Exception {
        // Buscar impresora instalada
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService impresora = null;

        for (PrintService ps : services) {
            if (ps.getName().equalsIgnoreCase(nombreImpresora)) {
                impresora = ps;
                break;
            }
        }

        if (impresora == null) {
            throw new Exception("No se encontró la impresora: " + nombreImpresora);
        }

        // Preparar documento
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        DocPrintJob job = impresora.createPrintJob();
        ByteArrayInputStream stream = new ByteArrayInputStream(texto.getBytes());
        Doc doc = new SimpleDoc(stream, flavor, null);

        // Enviar a impresora
        job.print(doc, new HashPrintRequestAttributeSet());
        stream.close();
    }
}