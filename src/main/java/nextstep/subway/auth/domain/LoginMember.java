package nextstep.subway.auth.domain;

import java.util.Objects;
import nextstep.subway.member.domain.MemberType;

public class LoginMember {
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

    public MemberType findMemberType() {
        if(Objects.isNull(age)){
            return MemberType.GUEST;
        }
        return MemberType.findType(age);
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
}
