package nextstep.subway.member.domain;

public class Age {

    public static final Age DEFAULT_AGE = Age.of(AgeGroup.DEFAULT.getMinRangeAge());

    private Integer age;

    private Age(Integer age) {
        this.age = age;
    }

    public static Age of(Integer age) {
        return new Age(age);
    }

    public Integer getAge() {
        return age;
    }
}
