package nextstep.subway.path.domain.fare.distance;

public interface DistanceFareFactory {
    DistanceFare getDistanceFare(int distance);

    static DistanceFareFactory instance() {
        return new DefaultDistanceFareFactory();
    }
}
