package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.dto.Fare;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FareDiscountResolver {
    private final List<FareDiscounter> fareDiscounters;

    public FareDiscountResolver(List<FareDiscounter> fareDiscounters) {
        this.fareDiscounters = fareDiscounters;
    }

    public Fare resolve(LoginMember loginMember, Fare fare) {
        for (FareDiscounter fareDiscounter : fareDiscounters) {
            if (fareDiscounter.canDiscount(loginMember)) {
                return fareDiscounter.discount(fare);
            }
        }
        return fare;
    }
}

