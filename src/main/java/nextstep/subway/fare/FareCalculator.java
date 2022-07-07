package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {
    private final DistanceFarePolicy distanceFarePolicy;
    private final LineFarePolicy lineFarePolicy;
    private final AgeFarePolicy ageFarePolicy;

    public FareCalculator(DistanceFarePolicy distanceFarePolicy, LineFarePolicy lineFarePolicy, AgeFarePolicy ageFarePolicy) {
        this.distanceFarePolicy = distanceFarePolicy;
        this.lineFarePolicy = lineFarePolicy;
        this.ageFarePolicy = ageFarePolicy;
    }

    public void calculate(Path path, LoginMember loginMember) {
        Fare distanceFare = distanceFarePolicy.calculate(new Distance(path.getDistance()));
        Fare lineFare = lineFarePolicy.calculate(path.getLines());
        Fare addedFare = distanceFare.add(lineFare);
        Fare resultFare = ageFarePolicy.calculate(addedFare, loginMember);
        path.updateFare(resultFare);
    }
}
