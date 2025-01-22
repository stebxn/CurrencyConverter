import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {


    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";


    private static final String[] CURRENCIES = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenido al Conversor de Monedas!");
        System.out.println("Monedas disponibles:");
        for (String currency : CURRENCIES) {
            System.out.println(currency);
        }

        System.out.print("Ingrese la moneda de origen: ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la moneda de destino: ");
        String toCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la cantidad a convertir: ");
        double amount = scanner.nextDouble();

        if (!isSupportedCurrency(fromCurrency) || !isSupportedCurrency(toCurrency)) {
            System.out.println("Una o ambas monedas no son soportadas. Por favor, inténtelo de nuevo.");
        } else {
            double convertedAmount = convertCurrency(fromCurrency, toCurrency, amount);
            if (convertedAmount != -1) {
                System.out.printf("%.2f %s son %.2f %s\n", amount, fromCurrency, convertedAmount, toCurrency);
            } else {
                System.out.println("Error al realizar la conversión. Por favor, inténtelo más tarde.");
            }
        }

        scanner.close();
    }

    private static boolean isSupportedCurrency(String currency) {
        for (String supportedCurrency : CURRENCIES) {
            if (supportedCurrency.equals(currency)) {
                return true;
            }
        }
        return false;
    }

    private static double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        try {

            URL url = new URL(API_URL + fromCurrency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();


            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject rates = jsonResponse.getJSONObject("rates");

            if (rates.has(toCurrency)) {
                double exchangeRate = rates.getDouble(toCurrency);
                return amount * exchangeRate;
            } else {
                System.out.println("La moneda de destino no está disponible en los datos del API.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return -1;
    }
}
