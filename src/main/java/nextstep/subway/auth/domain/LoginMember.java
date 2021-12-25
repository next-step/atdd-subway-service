package nextstep.subway.auth.domain;

public class LoginMember implements Member {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
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

    public AgeGroup getAgeGroup() {
        if (13 <= age && age < 19) {
            return AgeGroup.TEENAGER;
        }

        if (6 <= age && age < 13) {
            return AgeGroup.KID;
        }

        return AgeGroup.NORMAL;
    }

    @Override
    public boolean isLoginMember() {
        return true;
    }
}
