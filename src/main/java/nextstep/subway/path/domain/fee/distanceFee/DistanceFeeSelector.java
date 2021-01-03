package nextstep.subway.path.domain.fee.distanceFee;

public class DistanceFeeSelector {
    public static DistanceFee select(int distance) {
        if (distance <= LongDistanceFee.MIN_DISTANCE) {
            return new DefaultDistanceFee(distance);
        }

        if (distance >= LongDistanceFee.MAX_DISTANCE) {
            return new SuperLongDistanceFee(distance);
        }

        return new LongDistanceFee(distance);
    }
}
