package nextstep.subway.fare.discount;

public abstract class AgeDiscount {

    public static final int TEENAGER_START_AGE = 13;
    public static final int TEENAGER_END_AGE = 18;
    public static final int CHILDREN_START_AGE = 6;
    public static final int CHILDREN_END_AGE = 12;

    public AgeDiscount() {
    }

    public static AgeDiscount create(int age) {

        if (isTeenager(age)) {
            return new Teenager();
        }

        if (isChildren(age)) {
            return new Children();
        }

        return new Default();
    }

    private static boolean isChildren(int age) {
        return age >= CHILDREN_START_AGE && age <= CHILDREN_END_AGE;
    }

    private static boolean isTeenager(int age) {
        return age >= TEENAGER_START_AGE && age <= TEENAGER_END_AGE;
    }

    public abstract int getDeductionFare();

    public abstract double getDiscountRate();
}
