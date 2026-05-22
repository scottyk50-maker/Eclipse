import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class StockPriceHistoryExample {
    public static void main(String[] args) {
        String ticker = "AAPL"; // replace with your desired ticker symbol

        Calendar from = Calendar.getInstance();
        from.add(Calendar.MONTH, -1); // get prices for the last month
        Calendar to = Calendar.getInstance();

        try {
            List<HistoricalQuote> prices = YahooFinance.get(ticker).getHistory(from, to);
            for (HistoricalQuote price : prices) {
                System.out.println("Date: " + price.getDate().getTime()
                        + ", Close Price: " + price.getClose());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
