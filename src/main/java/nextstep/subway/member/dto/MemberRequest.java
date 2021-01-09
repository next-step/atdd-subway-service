package nextstep.subway.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.member.domain.Member;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @Min(1)
    private Integer age;

    public Member toMember() {
        return new Member(email, password, age);
    }
}
