package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;
import org.springframework.lang.Nullable;

public class OptionalLoginMember {

	@Nullable
	private final LoginMember loginMember;

	public static OptionalLoginMember notFound() {
		return new OptionalLoginMember(null);
	}

	public OptionalLoginMember(@Nullable LoginMember loginMember) {
		this.loginMember = loginMember;
	}

	public LoginMember orElseAuthenticationThrow() {
		if (isPresent()) {
			return loginMember;
		}

		throw new AuthorizationException("cannot find authorization!");
	}

	public boolean isPresent() {
		return loginMember != null;
	}
}
