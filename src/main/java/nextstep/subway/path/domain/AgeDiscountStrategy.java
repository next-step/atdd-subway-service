package nextstep.subway.path.domain;

@FunctionalInterface
public interface AgeDiscountStrategy {
    int discount(int fare);
}
