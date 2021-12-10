package nextstep.subway.auth.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private boolean guest = false;
    private AgeType ageType;

    public LoginMember(boolean guest) {
        this.guest = guest;
        this.ageType = AgeType.NONE;
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
        initAgeType();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public boolean isGuest() {
        return guest;
    }

    public AgeType getAgeType() {
        return ageType;
    }

    private void initAgeType() {
        if (AgeType.isKidAge(age)) {
            ageType = AgeType.KID;
            return;
        }

        if (AgeType.isAdolescentAge(age)) {
            ageType = AgeType.ADOLESCENT;
            return;
        }

        ageType = AgeType.NONE;
    }

    public enum AgeType {
        NONE, KID, ADOLESCENT;

        public static boolean isKidAge(int age) {
            return age >= 6 && age < 13;
        }

        public static boolean isAdolescentAge(int age) {
            return age >= 13 && age < 19;
        }

        public boolean isNone() {
            return this == NONE;
        }

        public boolean isKid() {
            return this == KID;
        }
    }

}
