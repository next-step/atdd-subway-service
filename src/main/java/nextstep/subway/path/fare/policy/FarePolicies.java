package nextstep.subway.path.fare.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.infrastructure.LoginMemberThreadLocal;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FarePolicies {

    private final List<FarePolicy> farePoliciesList;
    private final List<DiscountFarePolicy> discountFarePolicies;

    public FarePolicies(List<FarePolicy> farePoliciesList, List<DiscountFarePolicy> discountFarePolicies) {
        this.farePoliciesList = farePoliciesList;
        this.discountFarePolicies = discountFarePolicies;
    }

    public Fare calculate(Path path) {
        Fare fare = calculateFare(path);
        return calculateDiscount(fare);
    }

    private Fare calculateFare(Path path) {
        return farePoliciesList.stream()
                .map(farePolicy -> farePolicy.calculate(path))
                .reduce(Fare.ZERO, Fare::add);
    }

    private Fare calculateDiscount(Fare fare) {

        LoginMember loginMember = getLoginMember();

        for (DiscountFarePolicy discountFarePolicy : discountFarePolicies) {
            fare = discountFarePolicy.discount(loginMember, fare);
        }

        return fare;
    }

    private LoginMember getLoginMember() {
        return LoginMemberThreadLocal.get();
    }

}
