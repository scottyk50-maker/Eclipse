import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import java.util.Calendar;

public class TslaSupportResistanceExample {
    public static void main(String[] args) {
        // Set the start and end dates for historical data
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 2, 18);

        Calendar startCal = Calendar.getInstance();
        startCal.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth());

        
        // Fetch historical data for TSLA from Yahoo Finance API
        try {
            List<HistoricalQuote> history = YahooFinance.get("TSLA").getHistory(startCal, endCal, Interval.DAILY);
            
            // List to store support and resistance levels
            List<BigDecimal> supportLevels = new ArrayList<BigDecimal>();
            List<BigDecimal> resistanceLevels = new ArrayList<BigDecimal>();
            
            // Loop through historical data to find support and resistance levels
            for (int i = 1; i < history.size() - 1; i++) {
                HistoricalQuote current = history.get(i);
                HistoricalQuote prev = history.get(i-1);
                HistoricalQuote next = history.get(i+1);
                BigDecimal currentHigh = current.getHigh();
                BigDecimal currentLow = current.getLow();
                
                if (currentHigh.compareTo(prev.getHigh()) > 0 && currentHigh.compareTo(next.getHigh()) > 0) {
                    resistanceLevels.add(currentHigh);
                }
                
                if (currentLow.compareTo(prev.getLow()) < 0 && currentLow.compareTo(next.getLow()) < 0) {
                    supportLevels.add(currentLow);
                }
            }
            
            // Sort and output support and resistance levels
            Collections.sort(supportLevels);
            Collections.sort(resistanceLevels);
            System.out.println("Support Levels: " + supportLevels);
            System.out.println("Resistance Levels: " + resistanceLevels);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
