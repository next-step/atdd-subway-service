package nextstep.subway.fares.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Path;

public interface FarePolicy {
    void calculateFare(Fare fare, Path path, LoginMember loginMember);
}
