package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;

public class FarePolicy {

    private static final int BASE_FARE = 1250;
    private final LoginMember loginMember;
    private final Path path;
    private Fare fare;

    private FarePolicy(LoginMember loginMember, Path path) {
        this.loginMember = loginMember;
        this.path = path;
        this.fare = Fare.from(BASE_FARE);
    }

    public static FarePolicy of(LoginMember loginMember, Path path) {
        return new FarePolicy(loginMember, path);
    }

    public void calculateMaxExtraFare() {
        this.fare = fare.plus(path.getSections().getMaxExtraFare());
    }

    public void calculatorExtraFareByDistance() {
        this.fare = fare.plus(DistanceFarePolicy.valueOf(path.getDistance().value()));
    }

    public void calculateAgeDiscount() {
        if (!loginMember.isGuest()) {
            this.fare = AgeDiscountFarePolicy.valueOf(loginMember.getAge(), fare);
        }
    }

    public LoginMember getLoginMember() {
        return loginMember;
    }

    public Path getPath() {
        return path;
    }

    public Fare getFare() {
        return fare;
    }
}
