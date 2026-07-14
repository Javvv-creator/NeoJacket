package funcionalidades;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ImpresoraComprobante {

    public static void imprimirConLogo(String texto, String nombreImpresora) throws Exception {
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

        // Cargar logo desde recursos internos
        BufferedImage logo = ImageIO.read(
                ImpresoraComprobante.class.getResourceAsStream("/gui/image/logoFactura.png")
        );

        if (logo == null) {
            throw new Exception("No se pudo leer el logo desde recursos internos.");
        }

        // Crear un "lienzo" (imagen) que luego imprimimos
        // Ajusta el ancho a una aproximación para POS 80mm (≈ 576 puntos a 203dpi, depende del driver)
        int ancho = 576;      // aproximación
        int posYInicial = 10;

        // Texto: estimamos altura con métricas
        Font fuente = new Font("Monospaced", Font.PLAIN, 12);
        int padding = 10;
        int maxLinea = ancho - 2 * padding;

        // Preparar contexto
        BufferedImage canvas = new BufferedImage(ancho, 1200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

            // ====== DIBUJAR LOGO centrado ======
            // Escalar simple (sin “mejorar calidad”, solo ajusta tamaño)
            int logoMaxWidth = 340; // ajusta según tu gusto (centrado)
            int logoW = logo.getWidth();
            int logoH = logo.getHeight();

            double scale = (double) logoMaxWidth / logoW;
            int logoNewW = logoMaxWidth;
            int logoNewH = (int) Math.round(logoH * scale);

            // Evitar que sea gigante
            if (logoNewH < 1) {
                logoNewH = 1;
            }

            int xLogo = (ancho - logoNewW) / 2;
            int yLogo = posYInicial;

            Image scaled = logo.getScaledInstance(logoNewW, logoNewH, Image.SCALE_SMOOTH);
            g.drawImage(scaled, xLogo, yLogo, null);

            int y = yLogo + logoNewH + 15;

            // ====== DIBUJAR TEXTO ======
            g.setColor(Color.BLACK);
            g.setFont(fuente);

            // Dibujar líneas respetando saltos \n
            String[] lineas = texto.split("\\R");
            FontMetrics fm = g.getFontMetrics();

            int lineHeight = fm.getHeight();
            for (String linea : lineas) {
                // si línea muy larga, la recortamos
                String l = linea;
                // centrado opcional: tu texto ya trae asteriscos/espaciado
                // por ahora lo dibujamos centrado si no contiene muchos espacios
                // (puedes quitar esta lógica y dejarlo monoespaciado a la izquierda)
                int yLinea = y + fm.getAscent();
                if (yLinea + lineHeight > canvas.getHeight()) {
                    // si se pasa, cortamos (para no complicar). Puedes aumentar altura si quieres.
                    break;
                }

                // Centrar si la línea parece "titulo"
                int stringWidth = fm.stringWidth(l);
                int x = padding;
                // Centrado simple:
                if (stringWidth < maxLinea) {
                    x = (ancho - stringWidth) / 2;
                }
                g.drawString(l, x, yLinea);

                y += lineHeight;
            }

        } finally {
            g.dispose();
        }

        // Convertir canvas a InputStream y enviar por PrintService
     

        // Nota: algunos drivers aceptan InputStream tipo imagen como texto no.
        // Para evitar problemas, lo ideal es imprimir como servicio con un documento gráfico:
        // aquí usamos DocFlavor.BYTE_ARRAY (depende del driver).
        // Alternativa confiable: DocFlavor.SERVICE_FORMATTED.PRINTABLE
        // Pero para no complicar, usamos SERVICE_FORMATTED si está disponible.
        // Usar PrinterJob con Printable (más seguro)
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintService(impresora);

        printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }
            graphics.drawImage(canvas, 0, 0, null); // dibuja el canvas completo
            return Printable.PAGE_EXISTS;
        });

        PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
        printerJob.print(attrs);

    }
}
