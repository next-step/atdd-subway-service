package nextstep.subway.fare.discount;

public class Default extends AgeDiscount {
    
    @Override
    public int getDeductionFare() {
        return 0;
    }

    @Override
    public double getDiscountRate() {
        return 1;
    }
}
