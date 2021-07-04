package nextstep.subway.path.domain;

@FunctionalInterface
public interface DistanceFareStrategy {
    int calculateDistanceFare(int distance);
}
