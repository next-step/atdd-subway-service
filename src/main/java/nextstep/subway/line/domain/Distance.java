package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private static final int EXTRA_COST = 100;
    private static final int DEFAULT_COST = 1250;
    private static final int FIRST_SECTION = 10;
    private static final int FIRST_SECTION_COST_INCREMENT = 5;
    private static final int SECOND_SECTION = 50;
    private static final int SECOND_SECTION_COST_INCREMENT = 8;
    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public void validateLength(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("구간 길이는 0보다 큰 값을 입력해주세요.");
        }

        if (this.distance <= distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public int getDistance() {
        return distance;
    }

    public void setMinusDistance(int distance) {
        validateLength(distance);
        this.distance = Math.abs(this.distance - distance);
    }

    public int calculateCost() {
        if (distance > FIRST_SECTION) {
            return addExtraCost();
        }
        return DEFAULT_COST;
    }

    private int addExtraCost() {
        int cost = DEFAULT_COST + EXTRA_COST;

        if (distance > SECOND_SECTION) {
            return calculateOverSecondSection(cost);
        }

        return calculateOverFirstSection(cost);
    }

    private int calculateOverSecondSection(int cost) {
        cost = cost + (EXTRA_COST * SECOND_SECTION_COST_INCREMENT);
        int distance = this.distance - SECOND_SECTION;
        if (distance >= SECOND_SECTION_COST_INCREMENT) {
            cost = cost + ((distance / SECOND_SECTION_COST_INCREMENT) * EXTRA_COST);
        }
        return cost;
    }

    private int calculateOverFirstSection(int cost) {
        int distance = this.distance - FIRST_SECTION;
        if (distance >= FIRST_SECTION_COST_INCREMENT) {
            cost = cost + ((distance / FIRST_SECTION_COST_INCREMENT) * EXTRA_COST);
        }
        return cost;
    }
}
