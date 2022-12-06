package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

import java.util.List;

public class Price {

    private final int BASIC_DISTANCE = 10;
    private final int EXCESS_DISTANCE = 50;
    private final int BASIC_PRICE = 1250;

    private final int CHILDREN_START_AGE = 6;
    private final int CHILDREN_END_AGE = 13;
    private final int TEENAGER_START_AGE = 13;
    private final int TEENAGER_END_AGE = 19;

    private int price;

    public int value() {
        return price;
    }

    public void calculatorPrice(int distance, List<SectionEdge> sections, Integer age) {
        this.price = getStandardPriceByAge(age);
        this.price += calculatorPriceByDistance(distance);
        this.price += calculatorPriceBySections(sections);
    }

    private int getStandardPriceByAge(int age) {
        if (age >= CHILDREN_START_AGE && age < CHILDREN_END_AGE) {
            return (int) ((BASIC_PRICE - 350) * 0.5);
        }
        if (age >= TEENAGER_START_AGE && age < TEENAGER_END_AGE) {
            return (int) ((BASIC_PRICE - 350) * 0.8);
        }
        return BASIC_PRICE;
    }

    private int calculatorPriceByDistance(int distance) {
        if (distance <= BASIC_DISTANCE) {
            return 0;
        }
        if (distance > BASIC_DISTANCE && distance <= EXCESS_DISTANCE) {
            return calculateOverFare(5, distance - BASIC_DISTANCE);
        }
        return calculateOverFare(8, distance - BASIC_DISTANCE);
    }

    private int calculatorPriceBySections(List<SectionEdge> sections) {
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
