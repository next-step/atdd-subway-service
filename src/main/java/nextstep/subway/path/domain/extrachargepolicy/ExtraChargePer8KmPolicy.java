package nextstep.subway.path.domain.extrachargepolicy;

public class ExtraChargePer8KmPolicy implements ExtraChargePolicy {

    private final int distance;

    public ExtraChargePer8KmPolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculateOverFare() {
        return (int) ((Math.ceil((distance - 1) / 8.0) + 1) * 100);
    }
}
