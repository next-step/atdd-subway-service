package nextstep.subway.line.utils.PriceCalculator;

import java.util.List;
import nextstep.subway.line.domain.Line;

public interface PriceCalcHelper {

    boolean canSupport(int distance);

    int calculatePrice(int distance, List<Line> lines);
}
