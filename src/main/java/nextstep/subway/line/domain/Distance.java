package nextstep.subway.line.domain;

public class Distance  implements Comparable<Distance> {
    private static final int ZERO_VALUE = 0;

    private int distance;

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    public void add(Distance addValue) {
        this.distance += addValue.distance;
    }

    public int getDistance() {
        return distance;
    }

    public void subtract(Distance subtractValue) {
        if (this.compareTo(subtractValue) <= ZERO_VALUE) {
            throw new IllegalArgumentException("기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없습니다.");
        }
        this.distance -= subtractValue.distance;
    }

    @Override
    public int compareTo(Distance compareValue) {
        return this.distance - compareValue.distance;
    }
}
