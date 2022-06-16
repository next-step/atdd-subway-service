package nextstep.subway.line.utils.PriceCalculator;

import java.util.List;
import nextstep.subway.line.domain.Line;

public class Under10PriceCalcHelper implements PriceCalcHelper {

    private final static int MIN_DISTANCE = 0;
    private final static int MAX_DISTANCE = 10;
    private final static int DEFAULT_PRICE = 1_250;

    @Override
    public boolean canSupport(int distance) {
        return distance >= MIN_DISTANCE && distance <= MAX_DISTANCE;
    }

    @Override
    public int calculatePrice(int distance, List<Line> lines) {
        int linePrice = lines.stream().mapToInt(Line::getPrice).sum();
        return DEFAULT_PRICE + linePrice;
    }
}
