package nextstep.subway.line.application;

public class DistanceChargeCalculator {

    public static final int OVER_CHARGE = 100;

    public static int calculate(int charge, int distance) {

        if(distance > 10 && distance <= 50){
            int over = distance - 10;
            int overCharge = (int)((Math.ceil((over - 1) / 5) + 1) * OVER_CHARGE);
            return charge + overCharge;
        }

        if(distance > 50){
            int over = distance - 50;
            int overCharge = (int)((Math.ceil((over - 1) / 8) + 1) * OVER_CHARGE);
            return calculate(charge+overCharge, 50);
        }

        return charge;
    }


    private int calculateOverFare(int standard, int distance) {
        return (int) ((Math.ceil((distance - 1) / standard) + 1) * OVER_CHARGE);
    }
}
