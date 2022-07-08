package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {
    private final DistanceFarePolicy distanceFarePolicy;
    private final AgeFarePolicy ageFarePolicy;

    public FareCalculator(DistanceFarePolicy distanceFarePolicy, AgeFarePolicy ageFarePolicy) {
        this.distanceFarePolicy = distanceFarePolicy;
        this.ageFarePolicy = ageFarePolicy;
    }

    public void calculate(Path path, LoginMember loginMember) {
        Fare distanceFare = distanceFarePolicy.calculate(new Distance(path.getDistance()));
        Fare resultFare = ageFarePolicy.calculate(distanceFare, loginMember);
        path.updateFare(resultFare);
    }
}
