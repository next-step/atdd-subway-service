package nextstep.subway.path.domain;

public class PathDistance {
    private final Integer distance;

    public PathDistance(Integer distance) {
        validate(distance);
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

    private void validate(Integer distance) {
        if (distance == null || distance < 0) {
            throw new IllegalArgumentException("최단 경로의 거리는 0이상이어야합니다.");
        }
    }
}
