package nextstep.subway.fee.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.utils.SectionEdge;

import java.util.List;

import static nextstep.subway.fee.domain.PricePolicy.BASIC_PRICE;

public class AgePriceStrategy {

    private static PricePolicy pricePolicy = new PricePolicy();

    private static int price;

    public int value() {
        return price;
    }

    public static int calculatePrice(Integer age) {
        return getStandardPriceByAge(age);
    }

    private static int getStandardPriceByAge(int age) {
        if (pricePolicy.isChildren(age)) {
            return (int) ((BASIC_PRICE - 350) * 0.5);
        }
        if (pricePolicy.isTeenager(age)) {
            return (int) ((BASIC_PRICE - 350) * 0.2);
        }
        return 0;
    }

    private static int getMaxPriceBySectionStations(List<SectionEdge> sections) {
        return sections.stream()
                .map(SectionEdge::getSection)
                .map(Section::getLine)
                .mapToInt(Line::getAdditionalFee)
                .max()
                .orElse(0);
    }

}
