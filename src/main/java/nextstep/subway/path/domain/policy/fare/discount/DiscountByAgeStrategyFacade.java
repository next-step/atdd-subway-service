package nextstep.subway.path.domain.policy.fare.discount;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Fare;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiscountByAgeStrategyFacade {
    private List<DiscountByAgeStrategy> strategies = new ArrayList<>();

    public DiscountByAgeStrategyFacade() {
        strategies.add(new ChildDiscountByAgeStrategy());
        strategies.add(new TeenagerDiscountByAgeStrategy());
    }

    private Optional<DiscountByAgeStrategy> findStrategy(LoginMember member) {
        return strategies.stream()
                .filter(strategy -> strategy.isAvailable(member))
                .findFirst();
    }

    public int discountBy(LoginMember member, Fare fare) {
        int discount = 0;
        Optional<DiscountByAgeStrategy> findStrategy = findStrategy(member);
        if (findStrategy.isPresent()) {
            discount = findStrategy.get().discountBy(member, fare);
        }

        return discount;
    }
}
