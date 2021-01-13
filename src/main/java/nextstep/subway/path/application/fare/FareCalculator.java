package nextstep.subway.path.application.fare;

import nextstep.subway.line.domain.Section;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FareCalculator {

    private static final int BASIC_FARE = 1250;

    public int calculateFare(int distance) {
        return BASIC_FARE
                + getDistanceFarePolicy(distance).calculateOverFare(distance);
    }

    public int calculateFare(int distance, List<Section> sections) {
        return BASIC_FARE
                + getDistanceFarePolicy(distance).calculateOverFare(distance)
                + getMaxLineOverFare(sections);
    }

    public int calculateFare(int distance, List<Section> sections, int age) {
        int fare = BASIC_FARE
                + getDistanceFarePolicy(distance).calculateOverFare(distance)
                + getMaxLineOverFare(sections);

        return fare - getAgeOverFarePolicy(age).calculateDrawbackFare(fare);
    }

    private AgeDrawbackFarePolicy getAgeOverFarePolicy(int age) {
        return AgeDrawbackFarePolicy.valueOf(age);
    }

    private DistanceOverFarePolicy getDistanceFarePolicy(int distance) {
        return DistanceOverFarePolicy.valueOf(distance);
    }

    private int getMaxLineOverFare(List<Section> sections) {
        return sections.stream()
                .mapToInt(section -> section.getLine().getOverFare())
                .max().orElse(0);
    }
}
