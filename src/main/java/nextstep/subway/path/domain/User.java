package nextstep.subway.path.domain;

import nextstep.subway.member.domain.Member;

import java.util.function.Function;

public enum User {
    NONE(value -> value),
    ADULT(value -> value),
    YOUTH(value -> (value - 350) * 0.8),
    CHILD(value -> (value - 350) * 0.5),
    FREE(value -> value * 0);

    private Function<Double, Double> discounter;

    private User(Function<Double, Double> discounter) {
        this.discounter = discounter;
    }

    public static User typeOf(Member member) {
//        if (member == null) {
//            return User.NONE;
//        }

        if (member.isFree()) {
            return User.FREE;
        }

        if (member.isAdult()) {
            return User.ADULT;
        }

        if (member.isYouth()) {
            return User.YOUTH;
        }

        if (member.isChild()) {
            return User.CHILD;
        }
        return User.NONE;
    }

    public double discount(double value) {
        return discounter.apply(value);
    }
}
