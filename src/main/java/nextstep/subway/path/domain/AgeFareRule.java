package nextstep.subway.path.domain;

public class AgeFareRule {

    public static final int MINIMUM_AGE = 6;

    private static final int KID_LOWER_LIMIT = 6;
    private static final int KID_UPPER_LIMIT = 13;
    private static final int TEENAGER_LOWER_LIMIT = 13;
    private static final int TEENAGER_UPPER_LIMIT = 19;

    private static final AgeFareRule KID_RULE = new AgeFareRule(350, 0.5);
    private static final AgeFareRule TEENAGER_RULE = new AgeFareRule(350, 0.2);
    public static final AgeFareRule ADULT_RULE = new AgeFareRule(0, 0);

    private int deduction;
    private double discountPercent;

    private AgeFareRule(int deduction, double discountPercent) {
        this.deduction = deduction;
        this.discountPercent = discountPercent;
    }

    public static AgeFareRule of(int age) {
        if (age >= KID_LOWER_LIMIT && age < KID_UPPER_LIMIT)
            return KID_RULE;

        if (age >= TEENAGER_LOWER_LIMIT && age < TEENAGER_UPPER_LIMIT)
            return TEENAGER_RULE ;

        return ADULT_RULE;
    }

    public int getDeduction() {
        return deduction;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
