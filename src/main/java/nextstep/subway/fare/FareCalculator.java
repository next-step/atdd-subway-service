package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;

public interface FareCalculator {
    long fareCalculate(int distance, LoginMember member);
}
