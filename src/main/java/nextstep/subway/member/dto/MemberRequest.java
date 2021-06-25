package nextstep.subway.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.BaseRequest;
import nextstep.subway.member.domain.Member;

@Getter
@NoArgsConstructor
public class MemberRequest extends BaseRequest<Member> {
    private String email;
    private String password;
    private Integer age;

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    @Override
    public Member toEntity() {
        return new Member(email, password, age);
    }
}
