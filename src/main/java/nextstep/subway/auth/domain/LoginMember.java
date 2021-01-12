package nextstep.subway.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginMember {
	private Long id;
	private String email;
	private Integer age;
}
