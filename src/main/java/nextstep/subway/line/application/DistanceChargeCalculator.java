package nextstep.subway.line.application;

public class DistanceChargeCalculator {

    public static final int OVER_CHARGE = 100;
    public static final int TEN = 10;
    public static final int FIFTY = 50;
    public static final int OVER_PER_FIVE = 5;
    public static final int OVER_PER_EIGHT = 8;

    public static int calculate(int charge, int distance) {
        if(distance > FIFTY){
            charge += calculateOverCharge(OVER_PER_EIGHT, distance - FIFTY);
            distance = FIFTY;
        }

        if(distance > TEN && distance <= FIFTY){
            charge += calculateOverCharge(OVER_PER_FIVE, distance - TEN);
        }

        return charge;
    }


    private static int calculateOverCharge(int standard, int distance) {
        return (int) ((Math.ceil((distance - 1) / standard) + 1) * OVER_CHARGE);
    }
}
