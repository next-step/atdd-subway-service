package nextstep.subway.path.domain.policy;

public interface DiscountPolicy {

    int discount(int sourceFare, int age);
}
