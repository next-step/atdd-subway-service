package nextstep.subway.line.utils;

import java.util.List;
import nextstep.subway.line.domain.Line;

public class Over50PriceCalcHelper implements PathPriceCalcHelper {

    private static final int MIN_DISTANCE = 50;
    private static final int MAX_DISTANCE = Integer.MAX_VALUE;
    private static final int DEFAULT_PRICE = 1_250 + 800;
    private static final double CHECKING_DISTANCE = 8.0;
    private static final int PRICE_PER_CHECKING_DISTANCE = 100;

    @Override
    public boolean canSupport(int distance) {
        return distance > MIN_DISTANCE && distance <= MAX_DISTANCE;
    }

    @Override
    public int calculatePrice(int distance, List<Line> lines) {
        int linePrice = lines.stream().mapToInt(Line::getPrice).sum();
        int distancePrice = (int) Math.ceil((distance - MIN_DISTANCE) / CHECKING_DISTANCE)
            * PRICE_PER_CHECKING_DISTANCE;

        return DEFAULT_PRICE + linePrice + distancePrice;
    }
}
