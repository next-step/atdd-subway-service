package nextstep.subway.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.member.domain.Member;

@Getter
@NoArgsConstructor
public class MemberResponse {
    private Long id;
    private String email;
    private Integer age;

    public MemberResponse(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
    }
}
