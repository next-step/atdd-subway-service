package nextstep.subway.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.member.domain.Member;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse {
	private Long id;
	private String email;
	private Integer age;

	private MemberResponse(Long id, String email, Integer age) {
		this.id = id;
		this.email = email;
		this.age = age;
	}

	public static MemberResponse of(Member member) {
		return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
	}
}
