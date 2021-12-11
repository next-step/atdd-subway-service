package nextstep.subway.member.domain;

public enum MemberAgeType {
    NONE, KID, ADOLESCENT;

    public boolean isNone() {
        return this == NONE;
    }

    public boolean isKid() {
        return this == KID;
    }

    public static MemberAgeType getMemberAgeType(int age) {
        if (isKidAge(age)) {
            return MemberAgeType.KID;
        }

        if (isAdolescentAge(age)) {
            return MemberAgeType.ADOLESCENT;
        }

        return MemberAgeType.NONE;
    }

    private static boolean isKidAge(int age) {
        return age >= 6 && age < 13;
    }

    private static boolean isAdolescentAge(int age) {
        return age >= 13 && age < 19;
    }
}
