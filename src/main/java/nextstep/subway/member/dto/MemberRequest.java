package nextstep.subway.member.dto;

import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.Password;

public class MemberRequest {

    private String email;
    private String password;
    private Integer age;

    private MemberRequest() {
    }

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
        return Member.of(Email.from(email), Password.from(password), Age.from(age));
    }
}
