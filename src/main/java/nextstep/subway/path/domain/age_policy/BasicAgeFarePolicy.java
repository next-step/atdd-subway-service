package nextstep.subway.path.domain.age_policy;

public class BasicAgeFarePolicy implements AgeFarePolicy {

    @Override
    public int calculateByAge(int fare) {
        return fare;
    }
}
