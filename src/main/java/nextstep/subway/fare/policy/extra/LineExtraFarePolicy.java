package nextstep.subway.fare.policy.extra;

public class LineExtraFarePolicy implements ExtraFarePolicy {
    @Override
    public int addExtraFee(int extra) {
        return extra;
    }
}
