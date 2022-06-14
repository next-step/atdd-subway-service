package nextstep.subway.line.domain;

public class Age {
    private static final int DEFAULT_AGE = 19;

    private int age;

    private Age(int age) {
        if (age < 1) {
            throw new RuntimeException("나이는 1살부터 시작합니다.");
        }
        this.age = age;
    }

    public static Age of(int age) {
        return new Age(age);
    }

    public int getValue() {
        return age;
    }

    public boolean isNotAdult() {
        return age < DEFAULT_AGE;
    }
}
