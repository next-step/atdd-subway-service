package nextstep.subway.path.application;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.ExtraChargePolicy;
import org.springframework.stereotype.Service;

@Service
public class ExtraChargeService {

    public Money getExtraChargeFromDistance(Distance distance) {
        ExtraChargePolicy policy = ExtraChargePolicy.fromDistance(distance);
        return policy.getExtraCharge();
    }
}
