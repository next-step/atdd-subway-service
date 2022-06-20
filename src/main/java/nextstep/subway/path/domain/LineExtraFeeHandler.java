package nextstep.subway.path.domain;

import java.util.Set;
import nextstep.subway.line.domain.ExtraCharge;
import nextstep.subway.line.domain.Line;

public class LineExtraFeeHandler extends FeeHandler {
    private Set<Line> lines;

    protected LineExtraFeeHandler(FeeHandler feeHandler, Set<Line> lines) {
        super(feeHandler);
        this.lines = lines;
    }

    @Override
    public void calculate(FeeV2 fee) {
        addLineExtraFee(fee);
        super.calculate(fee);
    }

    private void addLineExtraFee(FeeV2 fee) {
        ExtraCharge extraCharge = ExtraCharge.of(0);
        for (Line line : this.lines) {
            extraCharge = ExtraCharge.max(extraCharge, line.getExtraCharge());
        }
        fee.add(extraCharge.getExtraCharge());
    }
}
