package nextstep.subway.path.domain;

public abstract class FeeHandler {
    private FeeHandler feeHandler;

    protected FeeHandler(FeeHandler feeHandler) {
        this.feeHandler = feeHandler;
    }

    public void calculate(Fee fee) {
        if (feeHandler != null) {
            feeHandler.calculate(fee);
        }
    }
}
