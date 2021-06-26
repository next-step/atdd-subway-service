package nextstep.subway.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.member.domain.Member;

@Getter
@NoArgsConstructor
public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member toMember() {
        return new Member(email, password, age);
    }
}
