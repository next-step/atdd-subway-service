package nextstep.subway.path.fare.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.infrastructure.LoginMemberThreadLocal;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;
import nextstep.subway.path.fare.policy.discount.DiscountFarePolicyType;
import nextstep.subway.path.fare.policy.extra.ExtraFarePolicyType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FarePolicyService {

    public Fare calculate(Path path) {
        Fare fare = calculateFare(path);
        return calculateDiscount(fare);
    }

    private Fare calculateFare(Path path) {
        return Arrays.stream(ExtraFarePolicyType.values())
                .map(farePolicy -> farePolicy.calculate(path))
                .reduce(Fare.ZERO, Fare::add);
    }

    private Fare calculateDiscount(Fare fare) {
        LoginMember loginMember = getLoginMember();

        for (DiscountFarePolicyType type : DiscountFarePolicyType.values()) {
            fare = type.discount(loginMember, fare);
        }
        return fare;
    }

    private LoginMember getLoginMember() {
        return LoginMemberThreadLocal.get();
    }
}
