package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    public static final int DISTANCE_SIZE = 1;
    int distance;

    protected Distance() {}

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if(distance < DISTANCE_SIZE){
            throw new IllegalArgumentException("거리는 1보다 작을 수 없습니다.");
        }
    }

    public int get() {
        return distance;
    }
}
