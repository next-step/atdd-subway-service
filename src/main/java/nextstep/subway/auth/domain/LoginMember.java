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
        if (AgeType.isKid(age)) {
            ageType = AgeType.KID;
            return;
        }

        if (AgeType.isAdolescent(age)) {
            ageType = AgeType.ADOLESCENT;
            return;
        }

        ageType = AgeType.NONE;
    }

    public enum AgeType {
        NONE, KID, ADOLESCENT;

        public static boolean isKid(int age) {
            return age >= 6 && age < 13;
        }

        public static boolean isAdolescent(int age) {
            return age >= 13 && age < 19;
        }

    }

}
