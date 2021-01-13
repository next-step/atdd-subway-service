package nextstep.subway.auth.application;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthorizationException extends RuntimeException {
	public AuthorizationException(String message) {
		super(message);
	}
}
