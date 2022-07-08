package nextstep.subway.Fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;

import java.util.List;

public class FareCalculator {
    private static final int BASIC_FARE = 1250;
    private static final Long BASIC_DISTANCE = 10L;
    private static final Long MIDDLE_DISTANCE = 50L;

    private static final int CHILD_START_AGE = 6;
    private static final int CHILD_END_AGE = 13;

    private static final int TEENAGER_START_AGE = 13;
    private static final int TEENAGER_END_AGE = 19;

    public static Fare calculate(List<Line> lines, Long distance, LoginMember loginMember) {
        if (loginMember.isNotLogin()) {
            return calculatorByNotLogin(lines, distance);
        }
        return calculatorByLogin(lines, distance, loginMember);
    }

    private static Fare calculatorByNotLogin(List<Line> lines, Long distance) {
        Fare fare = calculatorDefault(lines, distance);
        return fare;
    }

    private static Fare calculatorByLogin(List<Line> lines, Long distance, LoginMember loginMember) {
        Fare fare = calculatorDefault(lines, distance);
        fare = AgeDiscount(fare, loginMember);
        return fare;
    }

    private static Fare calculatorDefault(List<Line> lines, Long distance) {
        Fare fare = defaultFare(distance);
        fare = fare.plus(lineAdditionalFare(lines));
        return fare;
    }

    private static Fare defaultFare(Long distance) {
        int result = BASIC_FARE;

        if (distance > 10 && distance <= 50) {
            result += middleCalculator((int) (distance - BASIC_DISTANCE));
        }

        if (distance > 50) {
            result += middleCalculator((int) (MIDDLE_DISTANCE - BASIC_DISTANCE));
            result += lastCalculator((int) (distance - MIDDLE_DISTANCE));
        }

        return new Fare(result);
    }

    private static int middleCalculator(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private static int lastCalculator(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    private static Fare lineAdditionalFare(List<Line> lines) {
        int max = lines.stream()
                .mapToInt(Line::getAdditionalFare)
                .max()
                .orElse(0);

        return new Fare(max);
    }

    private static Fare AgeDiscount(Fare fare, LoginMember loginMember) {
        validAge(loginMember.getAge());
        if (loginMember.getAge() >= CHILD_START_AGE && loginMember.getAge() < CHILD_END_AGE) {
            return fare.discountChild();
        }

        if (loginMember.getAge() >= TEENAGER_START_AGE && loginMember.getAge() < TEENAGER_END_AGE) {
            return fare.discountTeenager();
        }
        return fare;
    }

    private static void validAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("나이는 0보다 작을 수 없습니다.");
        }
    }
}
