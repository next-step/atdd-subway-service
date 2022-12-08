package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

import java.util.List;

import static nextstep.subway.path.domain.PricePolicy.BASIC_DISTANCE;
import static nextstep.subway.path.domain.PricePolicy.BASIC_PRICE;

public class Price {

    private PricePolicy pricePolicy = new PricePolicy();

    private int price;

    public int value() {
        return price;
    }

    public void calculatePrice(int distance, List<SectionEdge> sections, Integer age) {
        this.price = getStandardPriceByAge(age);
        this.price += calculatorPriceByDistance(distance);
        this.price += getMaxPriceBySectionStations(sections);
    }

    private int getStandardPriceByAge(int age) {
        if (pricePolicy.isChildren(age)) {
            return (int) ((BASIC_PRICE - 350) * 0.5);
        }
        if (pricePolicy.isTeenager(age)) {
            return (int) ((BASIC_PRICE - 350) * 0.8);
        }
        return BASIC_PRICE;
    }

    private int calculatorPriceByDistance(int distance) {
        if (pricePolicy.isLessBasicDistance(distance)) {
            return 0;
        }
        if (pricePolicy.isGatherExcessDistance(distance)) {
            return calculateOverFare(8, distance - BASIC_DISTANCE);
        }
        return calculateOverFare(5, distance - BASIC_DISTANCE);
    }

    private int getMaxPriceBySectionStations(List<SectionEdge> sections) {
        return sections.stream()
                .map(SectionEdge::getSection)
                .map(Section::getLine)
                .mapToInt(Line::getAddedPrice)
                .max()
                .orElse(0);
    }

    private int calculateOverFare(int overStandard, int distance) {
        return (int) ((Math.ceil((distance - 1) / overStandard) + 1) * 100);
    }
}
