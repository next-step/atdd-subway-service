package nextstep.subway.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.member.domain.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public Member toMember() {
        return new Member(id, email, age);
    }
}
