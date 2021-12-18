package nextstep.subway.path.domain.overfare;

public class HighOverFare implements OverFare {

    @Override
    public int calculate(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
