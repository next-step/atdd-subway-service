package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Member;

public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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
