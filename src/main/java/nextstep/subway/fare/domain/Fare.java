package nextstep.subway.fare.domain;


public class Fare {

    private static final int DEFAULT_FARE = 1_250;
    private static final int EXTRA_CHARGE = 100;

    private int value;

    protected Fare() {
    }

    public Fare(int distance) {
        this.value = calculateExtraCharge(distance);
    }

    public Fare(int distance, int age) {
        this.value = isDiscountableAge(calculateExtraCharge(distance), age);
    }

    public Fare(int distance, int extraCharge, int age) {
        this.value = isDiscountableAge(calculateExtraCharge(distance) + extraCharge, age) ;
    }

    public static Fare of(int distance) {
        return new Fare(distance);
    }

    public static Fare of(int distance, int age) {
        return new Fare(distance, age);
    }

    public static Fare of(int distance, int extraCharge, int age) {
        return new Fare(distance, extraCharge, age);
    }

    private int calculateExtraCharge(int distance) {
        int fare = 0;
        if(isDefaultFare(distance)){
            fare = DEFAULT_FARE;
        }
        if(!isDefaultFare(distance) && !isOver50KM(distance)){
            fare += DEFAULT_FARE;
            fare += calculateMiddle(distance);
        }

        if(!isDefaultFare(distance) && isOver50KM(distance)){
            fare += DEFAULT_FARE;
            fare += calculateMiddle(distance);
            fare += calulateHigh(distance);
        }
        return fare;
    }

    private int calculateMiddle(int distance) {
        if(isOver50KM(distance)){
            distance -= (distance - 50);
        }
        return (distance -10) / 5 * EXTRA_CHARGE;
    }

    private int calulateHigh(int distance) {
        return (distance - 50) / 8 * EXTRA_CHARGE;
    }

    private boolean isDefaultFare(int distance) {
        return distance <= 10;
    }

    private boolean isOver50KM(int distance) {
        return distance > 50;
    }

    private int isDiscountableAge(int fare, int age) {
        int discountedFare = 0;
        if(isChildren(age)){
            discountedFare = discountChildren(fare, age);
        }
        if(isYouth(age)){
            discountedFare = discountYouth(fare, age);
        }
        return discountedFare != 0 ? discountedFare : fare;
    }

    private boolean isChildren(int age) {
        return 6 <= age  && age < 13;
    }

    private int discountChildren(int fare, int age) {
        return (int) (fare - 350) * 5 / 10;
    }

    private boolean isYouth(int age) {
        return 13 <= age  && age < 19;
    }

    private int discountYouth(int fare, int age) {
        return (int)(fare - 350) * 8 / 10;
    }

    public int getValue() {
        return value;
    }
}
