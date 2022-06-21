package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

import java.util.Objects;

public class Charge {
    public static final Charge BASE_CHARGE = new Charge(1250);
    private final int value;

    public Charge(int value) {
        this.value = value;
    }

    public Charge addAll(Surcharge... surcharges) {
        return add(Surcharges.of(surcharges).sum());
    }

    private Charge add(Surcharge surcharge) {
        return new Charge(value + surcharge.getValue());
    }

    private Charge minus(int value) {
        return new Charge(this.value - value);
    }

    private Charge multiply(double rate) {
        return new Charge((int) Math.ceil(this.value * rate));
    }

    public Charge discountBy(LoginMember loginMember) {
        if (loginMember.isTeenager()) {
            return minus(350).multiply(0.8);
        }
        if (loginMember.isChildren()) {
            return minus(350).multiply(0.5);
        }
        return this;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Charge)) return false;
        Charge charge = (Charge) o;
        return value == charge.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
