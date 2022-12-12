package nextstep.subway.path.domain;

import nextstep.subway.member.domain.Age;

public interface AgeStrategy {
    int discount(int fee, Age age);
}
