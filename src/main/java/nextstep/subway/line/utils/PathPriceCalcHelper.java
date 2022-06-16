package nextstep.subway.line.utils;

import java.util.List;
import nextstep.subway.line.domain.Line;

public interface PathPriceCalcHelper {

    boolean canSupport(int distance);

    int calculatePrice(int distance, List<Line> lines);
}
