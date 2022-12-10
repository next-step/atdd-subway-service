package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private Integer distance;

    protected Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public int getValue() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Distance distance1 = (Distance)o;

        return distance != null ? distance.equals(distance1.distance) : distance1.distance == null;
    }

    @Override
    public int hashCode() {
        return distance != null ? distance.hashCode() : 0;
    }

    public Distance minus(Distance distance) {
        if (this.distance <= distance.getValue()) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        return new Distance(this.getValue() - distance.getValue());
    }

    public Distance plus(Distance distance) {
        return new Distance(this.getValue() + distance.getValue());
    }
}
