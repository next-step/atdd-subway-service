package nextstep.subway.line.application.policy;


import nextstep.subway.line.domain.fare.Money;

public interface SubwayDistancePolicyCondition {

    boolean isSatisfiedBy(int distance);

    Money calculateFare(int distance);
}
