package nextstep.subway.policy;

import java.util.Arrays;

public enum AgeType {
    OTHER(0, 0, 0, 1),
    CHILD(6, 13, 350, 0.5),
    YOUTH(13, 19, 350, 0.2),
    ADULT(19, 65, 0, 1);


    private int more;
    private int under;
    private int discount;
    private double discountRate;

    AgeType(int more, int under, int discount, double discountRate) {
        this.more = more;
        this.under = under;
        this.discount = discount;
        this.discountRate = discountRate;
    }

    public static AgeType of(final int age) {
        return Arrays.stream(values())
                .filter(ageType -> ageType.isAge(age))
                .findFirst()
                .orElse(OTHER);
    }

    private boolean isAge(final int age) {
        return more <= age && age < under;
    }

    public int discount(int fare) {
        if (this.equals(ADULT)) {
            return fare;
        }

        return (int) ((fare - discount) - ((fare - discount) * discountRate));
    }
}
