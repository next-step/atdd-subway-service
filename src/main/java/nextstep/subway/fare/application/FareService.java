package nextstep.subway.fare.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Service;

@Service
public class FareService {

    public Fare calculateFare(LoginMember member, Path path) {
        FareCalculator fareCalculator = new FareCalculator(member, path);
        return fareCalculator.calculate();
    }
}
