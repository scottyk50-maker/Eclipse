import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StockPrice {

  public static void main(String[] args) {
    try {
      // Specify the stock symbol for the company whose stock price you want to retrieve
      String symbol = "AAPL";

      // Send a GET request to the Yahoo Finance API
      URL url = new URL("https://finance.yahoo.com/quote/" + symbol + "?p=" + symbol);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");

      // Read the response from the Yahoo Finance API
      BufferedReader in = new BufferedReader(
          new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      // Parse the response to extract the stock price
      String price = parsePrice(response.toString());
      System.out.println("Stock price: " + price);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String parsePrice(String response) {
	return response;
    // TODO: Implement this method to extract the stock price from the response
  }
}