package nextstep.subway.path.domain.age_policy;

import java.util.Arrays;

public enum AgeFareInformation {

    CHILD(6, 13, new ChildAgeFarePolicy()),
    TEEN(13, 19, new TeenAgeFarePolicy()),
    BASIC(new BasicAgeFarePolicy());

    private int minAge;
    private int maxAge;
    private AgeFarePolicy ageFarePolicy;

    AgeFareInformation(AgeFarePolicy ageFarePolicy) {
        this.ageFarePolicy = ageFarePolicy;
    }

    AgeFareInformation(int minAge, int maxAge, AgeFarePolicy ageFarePolicy) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.ageFarePolicy = ageFarePolicy;
    }

    public static AgeFarePolicy ageFarePolicy(int age) {
        return Arrays.stream(values()).filter(ageFare -> ageFare.minAge() <= age && age <= ageFare.maxAge())
                .findFirst().orElse(BASIC).ageFarePolicy();
    }

    public int minAge() {
        return minAge;
    }

    public int maxAge() {
        return maxAge;
    }

    public AgeFarePolicy ageFarePolicy() {
        return ageFarePolicy;
    }
}
