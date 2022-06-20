package nextstep.subway.path.domain;

public abstract class FeeHandler {
    private FeeHandler feeHandler;

    protected FeeHandler(FeeHandler feeHandler) {
        this.feeHandler = feeHandler;
    }

    public void calculate(FeeV2 fee) {
        if (feeHandler != null) {
            feeHandler.calculate(fee);
        }
    }
}
