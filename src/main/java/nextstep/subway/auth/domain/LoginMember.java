package nextstep.subway.auth.domain;

import nextstep.subway.fare.domain.AgeFarePolicy;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Email;

public class LoginMember {

    private static final Age ADULT = AgeFarePolicy.ADULT.getMinAge();
    private static final LoginMember nonMember = new LoginMember(ADULT);

    private Long id;
    private String email;
    private Age age;

    private LoginMember() {}

    public LoginMember(Long id, Email email, Age age) {
        this.id = id;
        this.email = email.value();
        this.age = age;
    }

    private LoginMember(Age age) {
        this.age = age;
    }

    public static LoginMember nonMember() {
        return nonMember;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Age getAge() {
        return age;
    }
}
