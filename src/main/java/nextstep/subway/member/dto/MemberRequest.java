package nextstep.subway.member.dto;

import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Member;

public class MemberRequest {
    private String email;
    private String password;
    private Age age;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password, Age age) {
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

    public Age getAge() {
        return age;
    }

    public Member toMember() {
        return new Member(email, password, age);
    }
}
