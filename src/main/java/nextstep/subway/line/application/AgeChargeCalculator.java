package nextstep.subway.line.application;

public class AgeChargeCalculator {

    public static final int AGE_SIX = 6;
    public static final int AGE_THIRTEEN = 13;
    public static final int AGE_NINETEEN = 19;
    public static final int AGE_SIXTY_FIVE = 65;
    public static final int BASIC_DISCOUNT = 350;
    public static final int FREE_CHARGE = 0;
    public static final int CHILDREN_DISCOUNT_RATE = 50;
    public static final int TEENAGER_DISCOUNT_RATE = 20;
    public static final int PERCENTAGE = 100;

    public static int calculate(int charge, int age) {

        if (isTeenager(age)) {
            return (charge - BASIC_DISCOUNT) * (PERCENTAGE - TEENAGER_DISCOUNT_RATE) / PERCENTAGE;
        }

        if (isChildren(age)) {
            return (charge - BASIC_DISCOUNT) * (PERCENTAGE - CHILDREN_DISCOUNT_RATE) / PERCENTAGE;
        }
        if (isBabyOrOld(age)) {
            return FREE_CHARGE;
        }
        return charge;
    }

    private static boolean isTeenager(int age) {
        return age >= AGE_THIRTEEN && age < AGE_NINETEEN;
    }

    private static boolean isChildren(int age) {
        return age >= AGE_SIX && age < AGE_THIRTEEN;
    }

    private static boolean isBabyOrOld(int age) {
        return age < AGE_SIX || age >= AGE_SIXTY_FIVE;
    }
}
