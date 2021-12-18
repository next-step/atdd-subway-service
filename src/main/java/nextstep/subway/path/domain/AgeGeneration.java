package nextstep.subway.path.domain;

public enum AgeGeneration {
    CHILD_GENERATION(13),
    YOUTH_GENERATION(19);

    private final int age;

    AgeGeneration(final int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
