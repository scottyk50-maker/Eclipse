import java.util.Random;

public class Roulette2 {
    public static void main(String[] args) {
        Random rand = new Random();
        int money = 1000;
        int bet = 10;
        int spins = 100;
        int wins = 0;
        int losses = 0;
        int partial_losses = 0;
        int break_even = 0; 
        int wins_ir = 0;
        int losses_ir = 0;       
        int partial_losses_ir = 0;
        int break_even_ir = 0; 
        int top_line_bet = 3;
        int bottom_line_bet = 2;
        int bottom_bet = 1; 
        int total_bet = top_line_bet + bottom_line_bet + bottom_bet;
        
        for (int i = 0; i < spins; i++) {

        	int number = rand.nextInt(37);

        	System.out.println("Number: " + number);
            
            if (number == 0) {
                losses++;
                money += -6;
            } else if (number == 1) {
            	partial_losses++;
                money += -2;
            } else if (number == 2) {
                losses++;
                money += -6;
            } else if (number == 3) {
            	break_even++;
            } else if (number == 4) {
            	partial_losses++;
                money += -2;
            } else if (number == 5) {
                losses++;
                money += -6;
      		} else if (number == 6) {
            	break_even++;
      		} else if (number == 7) {
            	partial_losses++;
                money += -2;
      		} else if (number == 8) {
                losses++;
                money += -6;
      		} else if (number == 9) {
            	break_even++;
      		} else if (number == 10) {
            	partial_losses++;
                money += -2;
      		} else if (number == 11) {
                losses++;
                money += -6;
      		} else if (number == 12) {
            	break_even++;
      		} else if (number == 13) {
            	break_even++;
      		} else if (number == 14) {
            	partial_losses++;
                money += -4;
      		} else if (number == 15) {
      			wins++;
                money += 2;
      		} else if (number == 16) {
            	break_even++;
      		} else if (number == 17) {
            	partial_losses++;
                money += -4;
      		} else if (number == 18) {
      			wins++;
                money += 2;
      		} else if (number == 19) {
            	break_even++;
      		} else if (number == 20) {
            	partial_losses++;
                money += -4;
      		} else if (number == 21) {
      			wins++;
                money += 2;
      		} else if (number == 22) {
            	break_even++;
      		} else if (number == 23) {
            	partial_losses++;
                money += -4;
      		} else if (number == 24) {
      			wins++;
                money += 2;
      		} else if (number == 25) {
            	partial_losses++;
                money += -2;
      		} else if (number == 26) {
                losses++;
                money += -6;
      		} else if (number == 27) {
            	break_even++;
      		} else if (number == 28) {
            	partial_losses++;
                money += -2;
      		} else if (number == 29) {
                losses++;
                money += -6;
      		} else if (number == 30) {
            	break_even++;
      		} else if (number == 31) {
            	partial_losses++;
                money += -2;
      		} else if (number == 32) {
                losses++;
                money += -6;
      		} else if (number == 33) {
            	break_even++;
      		} else if (number == 34) {
            	partial_losses++;
                money += -2;
      		} else if (number == 35) {
                losses++;
                money += -6;
      		} else if (number == 36) {
            	break_even++;
      		} else if (number == 37) {
                losses++;
                money += -6;
      		}
        }
        
        System.out.println("Wins: " + wins);
        System.out.println("Losses: " + losses);
        System.out.println("Partial Losses: " + partial_losses);
        System.out.println("Break Even: " + break_even);
        System.out.println("Money: $" + money);
    }
}