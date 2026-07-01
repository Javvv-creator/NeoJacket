package funcionalidades;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class API {

    private static final String API_KEY = "d075065f5bbfd083adc4187a";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    /**
     * Obtiene de golpe un mapa con las tasas de las monedas solicitadas basándose en una moneda origen.
     * Ejemplo: para "USD" y ["EUR", "MXN"], devolverá un mapa con { "EUR" -> "0.92", "MXN" -> "17.10" }
     */
    public static Map<String, String> obtenerTasas(String monedaBase, String[] monedasDestino) throws Exception {
        Map<String, String> resultados = new HashMap<>();
        String urlCompleta = BASE_URL + monedaBase;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompleta))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error HTTP: " + response.statusCode());
        }

        String json = response.body();

        if (!json.contains("\"result\":\"success\"")) {
            throw new RuntimeException("Respuesta de API inválida.");
        }

        // Buscamos cada moneda solicitada dentro del JSON de respuesta
        for (String moneda : monedasDestino) {
            String claveBusqueda = "\"" + moneda + "\":";
            int index = json.indexOf(claveBusqueda);
            
            if (index != -1) {
                int inicioValor = index + claveBusqueda.length();
                int finValor = json.indexOf(",", inicioValor);
                int finLlave = json.indexOf("}", inicioValor);
                
                if (finValor == -1 || finValor > finLlave) {
                    finValor = finLlave;
                }
                
                String valorTasa = json.substring(inicioValor, finValor).trim();
                resultados.put(moneda, valorTasa);
            } else {
                resultados.put(moneda, "N/A");
            }
        }

        return resultados;
    }
}
    
