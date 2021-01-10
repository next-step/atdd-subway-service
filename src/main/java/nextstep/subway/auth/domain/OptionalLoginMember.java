package nextstep.subway.auth.domain;

import org.springframework.lang.Nullable;

import java.util.Optional;

public class OptionalLoginMember {

	@Nullable
	private final LoginMember loginMember;

	public OptionalLoginMember(@Nullable LoginMember loginMember) {
		this.loginMember = loginMember;
	}

	public Optional<LoginMember> optional() {
		return Optional.ofNullable(loginMember);
	}
}
