package nextstep.subway.path.domain;

public class Fee {

    public static final int  MINUS_AMOUNT = 350;
    private static final int DEFAULT_FEE = 1250;
    private static final int SECOND_OVER_POINT = 50;
    private static final int FIRST_OVER_POINT = 10;
    private static final int OVER_FEE = 100;
    private static final int FIRST_OVER_DISTANCE = 5;
    private static final int SECOND_OVER_DISTANCE = 8;

    private int fee;
    private int distance;
    private DiscountType discountType;

    public Fee(int addFee, int distance, DiscountType discountType) {
        this.fee = DEFAULT_FEE + addFee;
        this.distance = distance;
        this.discountType = discountType;
    }

    public int calculate() {
        applyFirstOverFare();
        applySecondOverFare();
        return applyDiscount();
    }

    private void applyFirstOverFare() {
        if (distance > FIRST_OVER_POINT) {
            int overFare = calculateFirstOverFare((distance % (SECOND_OVER_POINT + 1)) - FIRST_OVER_POINT);
            fee += overFare;
        }
    }

    private int calculateFirstOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / FIRST_OVER_DISTANCE) + 1) * OVER_FEE);
    }

    private void applySecondOverFare() {
        if (distance > SECOND_OVER_POINT) {
            int overFare = calculateSecondOverFare(distance - SECOND_OVER_POINT);
            fee += overFare;
        }
    }

    private int calculateSecondOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / SECOND_OVER_DISTANCE) + 1) * OVER_FEE);
    }

    private int applyDiscount() {
        int getDiscountAmount = (int) ((fee - MINUS_AMOUNT) * discountType.getDiscountPercent());
        return fee - getDiscountAmount;
    }

}
