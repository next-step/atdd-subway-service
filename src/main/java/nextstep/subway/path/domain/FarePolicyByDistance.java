package nextstep.subway.path.domain;

/**
 * - 기본운임(10㎞ 이내) : 기본운임 1,250원
 * - 이용 거리초과 시 추가운임 부과
 *     - 10km초과∼50km까지(5km마다 100원)
 *     - 50km초과 시 (8km마다 100원)
 */
public class FarePolicyByDistance implements FarePolicy{
    private final double distance;

    public FarePolicyByDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public double calculate(double fare) {
        if (distance > 10 && distance <= 50) {
            return fare + ((Math.ceil((distance - 10) / 5)) * 100);
        }
        if (distance > 50) {
            return fare + 800 + ((Math.ceil((distance - 50) / 8)) * 100);
        }
        return fare;
    }
}
