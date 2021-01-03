package nextstep.subway.path.domain;

public enum AgeDiscounter {
    NONE,
    TEEN,
    KID;

    private static final Integer MIN_KID = 5;
    private static final Integer MAX_KID = 13;
    private static final Integer MIN_TEEN = 12;
    private static final Integer MAX_TEEN = 19;

    public static AgeDiscounter find(Integer age) {
        if (age > MIN_KID && age < MAX_KID) {
            return AgeDiscounter.KID;
        }
        if (age > MIN_TEEN && age < MAX_TEEN) {
            return AgeDiscounter.TEEN;
        }
        return AgeDiscounter.NONE;
    }
}
