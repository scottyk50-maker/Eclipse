import java.util.ArrayList;

public class SupportResistanceExample {
    public static void main(String[] args) {
        // Historical data of stock prices
        double[] stockPrices = {100, 105, 95, 110, 120, 130, 125, 140, 145, 155};

        // List to store support and resistance levels
        ArrayList<Double> supportLevels = new ArrayList<Double>();
        ArrayList<Double> resistanceLevels = new ArrayList<Double>();

        // Finding support and resistance levels
        double prevHigh = 0;
        double prevLow = 0;
        double currentHigh = 0;
        double currentLow = stockPrices[0];

        for (int i = 1; i < stockPrices.length; i++) {
            if (stockPrices[i] > currentHigh) {
                currentHigh = stockPrices[i];
            } else if (stockPrices[i] < currentLow) {
                currentLow = stockPrices[i];
            }

            if (i == stockPrices.length - 1) {
                if (currentHigh > prevHigh) {
                    resistanceLevels.add(currentHigh);
                }

                if (currentLow < prevLow) {
                    supportLevels.add(currentLow);
                }
            } else if (i == 1 || i == 2) {
                prevHigh = currentHigh;
                prevLow = currentLow;
                currentHigh = 0;
                currentLow = stockPrices[i];
            } else {
                if (currentHigh > prevHigh && currentHigh > stockPrices[i - 1]) {
                    resistanceLevels.add(currentHigh);
                    prevHigh = currentHigh;
                    currentHigh = 0;
                    currentLow = stockPrices[i];
                }

                if (currentLow < prevLow && currentLow < stockPrices[i - 1]) {
                    supportLevels.add(currentLow);
                    prevLow = currentLow;
                    currentLow = stockPrices[i];
                    currentHigh = stockPrices[i];
                }
            }
        }

        // Output support and resistance levels
        System.out.println("Support Levels: " + supportLevels);
        System.out.println("Resistance Levels: " + resistanceLevels);

        // Determine when the stock price goes above resistance or below support levels
        for (int i = 0; i < stockPrices.length; i++) {
            if (resistanceLevels.contains(stockPrices[i])) {
                System.out.println("Stock price goes above resistance level at index " + i);
            }

            if (supportLevels.contains(stockPrices[i])) {
                System.out.println("Stock price goes below support level at index " + i);
            }
        }
    }
}
