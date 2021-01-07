package nextstep.subway.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.member.domain.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private String email;
    private String password;
    private Integer age;

    public Member toMember() {
        return new Member(email, password, age);
    }
}
