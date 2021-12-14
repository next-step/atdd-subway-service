package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Member;

public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public MemberRequest() {
    }

    private MemberRequest(final String email, final String password, final Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static MemberRequest of(final String email, final String password, final Integer age) {
        return new MemberRequest(email, password, age);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public Member toMember() {
        return Member.of(email, password, age);
    }
}
