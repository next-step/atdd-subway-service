package nextstep.subway.path.domain;

public class BaseCharge extends Charge {
    public static final BaseCharge BASE_CHARGE = new BaseCharge(1250);

    private BaseCharge(int value) {
        super(value);
    }
}
