package nextstep.subway.auth.domain;

public class LoginMember {
    public static LoginMember DEFAULT_USER = new LoginMember();

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {}

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

    public double getDiscountRate() {
        if (null == age || age == 0) {
            return 1;
        }
        return DiscountRate.findDiscountRateByAge(age).getDicountRate();
    }
}
