package nextstep.subway.path.domain;

public class PathFare {
    private final int fare;

    public PathFare(Integer fare) {
        validate(fare);
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }

    private void validate(Integer fare) {
        if (fare == null) {
            throw new IllegalArgumentException("지하철 요금이 존재하지 않습니다.");
        }
        if (fare < 0) {
            throw new IllegalArgumentException("지하철 요금은 0이상이어야합니다.");
        }
    }
}
