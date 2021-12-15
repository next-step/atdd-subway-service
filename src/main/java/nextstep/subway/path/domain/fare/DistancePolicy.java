package nextstep.subway.path.domain.fare;

public class DistancePolicy {

    public int getOverDistance(int distance) {
        double overDistance = ExtraDistance.getOverDistance(distance);
         return (int) Math.ceil(overDistance);
    }
}
