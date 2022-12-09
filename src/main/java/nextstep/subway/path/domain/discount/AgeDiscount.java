package nextstep.subway.path.domain.discount;

public abstract class AgeDiscount {

    public static final int TEENAGER_START_AGE = 13;
    public static final int TEENAGER_END_AGE = 18;
    public static final int CHILDREN_START_AGE = 6;
    public static final int CHILDREN_END_AGE = 12;
    private final int fare;

    public AgeDiscount(int fare) {
        this.fare = fare;
    }

    public static AgeDiscount create(int fare, int age) {

        if (isTeenager(age)) {
            return new Teenager(fare);
        }

        if (isChildren(age)) {
            return new Children(fare);
        }

        return new Default(fare);
    }

    private static boolean isChildren(int age) {
        return age >= CHILDREN_START_AGE && age <= CHILDREN_END_AGE;
    }

    private static boolean isTeenager(int age) {
        return age >= TEENAGER_START_AGE && age <= TEENAGER_END_AGE;
    }

    public abstract int discount();

    public int getFare() {
        return this.fare;
    }
}
