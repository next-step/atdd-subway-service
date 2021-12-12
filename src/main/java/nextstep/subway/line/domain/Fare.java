package nextstep.subway.line.domain;

import nextstep.subway.member.domain.Age;

public class Fare {

    private PathResult pathResult;
    private Age age;

    public Fare(PathResult pathResult, Age age) {
        this.pathResult = pathResult;
        this.age = age;
    }

    public static Fare of(PathResult pathResult, Age age) {
        return new Fare(pathResult, age);
    }

    public int getDistance() {
        return pathResult.getWeight();
    }

    public Money getMaxAdditionalFare() {
        return pathResult.getMaxAdditionalFare();
    }

    public Age getAge() {
        return age;
    }

}
