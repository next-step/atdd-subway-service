package nextstep.subway.path.domain.extrachargepolicy;

public class ExtraChargePer5KmPolicy implements ExtraChargePolicy {

    private final int distance;

    public ExtraChargePer5KmPolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculateOverFare() {
        return (int) ((Math.ceil((distance - 1) / 5.0) + 1) * 100);
    }
}
