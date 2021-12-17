package nextstep.subway.member.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.KindFarePolicy;

import java.util.stream.Stream;

public enum MemberKind {
    INFANCY(1, 6, basicFare -> Fare.createZeroFare()),
    CHILD(6, 13, basicFare -> basicFare.minus(Fare.of(350)).multiply(0.5f)),
    TEENAGER(13, 19, basicFare -> basicFare.minus(Fare.of(350)).multiply(0.8f)),
    ADULT(19, 200, basicFare -> basicFare);

    private final int startAge;
    private final int endAge;
    private final KindFarePolicy kindFarePolicy;

    MemberKind(int startAge, int endAge, KindFarePolicy kindFarePolicy) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.kindFarePolicy = kindFarePolicy;
    }

    public static MemberKind of(int age) {
        return Stream.of(MemberKind.values())
                .filter(it -> isMatchAge(age, it))
                .findFirst()
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.IT_IS_WRONG_AGE));
    }

    private static boolean isMatchAge(int age, MemberKind it) {
        return it.startAge <= age && age < it.endAge;
    }

    public Fare discountFare(Fare basicFare) {
        return kindFarePolicy.calculateFare(basicFare);
    }


}
