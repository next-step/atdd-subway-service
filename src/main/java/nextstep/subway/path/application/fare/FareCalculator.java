package nextstep.subway.path.application.fare;

import nextstep.subway.auth.domain.LoginMember;
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

    public int calculateFare(int distance, List<Section> sections, LoginMember loginMember) {
        int fare = BASIC_FARE
                + getDistanceFarePolicy(distance).calculateOverFare(distance)
                + getMaxLineOverFare(sections);

        return applyDrawbackFare(loginMember, fare);
    }

    private int applyDrawbackFare(LoginMember loginMember, int fare) {
        if(loginMember.isAuthorized()) {
            AgeDrawbackFarePolicy ageOverFarePolicy = getAgeOverFarePolicy(loginMember.getAge());
            return fare - ageOverFarePolicy.calculateDrawbackFare(fare);
        }

        return fare;
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
