package nextstep.subway.path.domain;

@FunctionalInterface
public interface AgeDiscountPolicy {

    int discount(int fare);
}
