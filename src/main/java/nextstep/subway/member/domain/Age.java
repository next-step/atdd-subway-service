package nextstep.subway.member.domain;

public class Age {

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
