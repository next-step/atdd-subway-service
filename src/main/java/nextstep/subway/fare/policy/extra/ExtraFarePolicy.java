package nextstep.subway.fare.policy.extra;

@FunctionalInterface
public interface ExtraFarePolicy {
    int addExtraFee(int extra);
}
