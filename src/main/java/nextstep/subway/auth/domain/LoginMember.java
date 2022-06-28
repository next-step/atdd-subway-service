package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeGroup;
import nextstep.subway.member.domain.UserType;

public class LoginMember {
    private Long id;
    private String email;
    private Age age;
    private UserType userType;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age, UserType userType) {
        this.id = id;
        this.email = email;
        this.age = new Age(age);
        this.userType = userType;
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

    public AgeGroup getAgeGroup() {
        return AgeGroup.getAgeGroupByAge(this.age);
    }

    public UserType getUserType() {
        return userType;
    }
}
