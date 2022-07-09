package nextstep.subway.fare.policy;

public enum AgeType {
    TEENAGERS,
    CHILDREN,
    NORMAL;

    public static AgeType of(int age) {
        if (age >= 6 && age < 13) {
            return CHILDREN;
        }

        if (age >= 13 && age < 19) {
            return TEENAGERS;
        }

        return NORMAL;
    }

    public boolean isTeenagers(AgeType ageType) {
        return TEENAGERS.equals(ageType);
    }

    public boolean isChildren(AgeType ageType) {
        return CHILDREN.equals(ageType);
    }

    public boolean isNormal(AgeType ageType) {
        return NORMAL.equals(ageType);
    }
}
