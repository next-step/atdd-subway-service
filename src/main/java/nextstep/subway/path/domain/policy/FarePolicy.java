package nextstep.subway.path.domain.policy;

@FunctionalInterface
public interface FarePolicy {
    int calculate(int fare);
}
