package nextstep.subway.auth.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenRequest {
	@NotNull
	private final String email;
	@NotNull
	private final String password;
}
