package nextstep.subway.path.policy;

import nextstep.subway.line.domain.Line;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class DefaultFarePolicy implements FarePolicy {
    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_ADDED_FARE = 100;
    private static final FareSection[] distanceTable = {FareSection.FIRST, FareSection.SECOND};

    @Override
    public int calculateOverFare(Set<Line> lines, int distance) {
        int maxFare = lines.stream()
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElse(0);
        return maxFare + accumulateFare(distanceTable.length - 1, distance, DEFAULT_FARE);
    }

    @Override
    public int calculateOverFare(Set<Line> lines, int distance, int age) {
        return AgeSection.discount(calculateOverFare(lines, distance), age);
    }

    private int accumulateFare(int index, int distance, int fare) {
        if (index < 0) {
            return fare;
        }

        int overFare = 0;
        int overDistance = distance - distanceTable[index].getDistance();
        if  (overDistance > 0) {
            overFare = calculateOverFare(overDistance, distanceTable[index].getSection());
            distance -= overDistance;
        }

        return accumulateFare(index - 1, distance, fare + overFare);
    }

    private int calculateOverFare(int distance, int section) {
        return (int) ((Math.ceil((distance - 1) / section) + 1) * DEFAULT_ADDED_FARE);
    }

    enum FareSection {
        FIRST (10, 5),
        SECOND (50, 8);

        private final int distance;
        private final int section;

        FareSection(int distance, int section) {
            this.distance = distance;
            this.section = section;
        }

        public int getDistance() {
            return distance;
        }

        public int getSection() {
            return section;
        }
    }

    enum AgeSection {
        CHILD (6, 12, 50),
        YOUTH (13, 18, 20),
        ADULT (19, Integer.MAX_VALUE, 0);

        private final static int DEDUCTIBLE = 350;
        private int startAge;
        private int endAge;
        private int discountRate;

        AgeSection(int startAge, int endAge, int discountRate) {
            this.startAge = startAge;
            this.endAge = endAge;
            this.discountRate = discountRate;
        }

        static int discount(int fare, int age) {
            AgeSection ageSection = Arrays.stream(values())
                    .filter(section -> section.isAssigned(age))
                    .findFirst()
                    .orElse(ADULT);
            int discount = (fare - DEDUCTIBLE) * ageSection.discountRate / 100;
            return fare - discount;
        }

        private boolean isAssigned(int age) {
            return startAge <= age && endAge >= age;
        }
    }
}
