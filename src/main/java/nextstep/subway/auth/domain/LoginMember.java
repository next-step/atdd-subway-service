package nextstep.subway.auth.domain;

public class LoginMember extends Member {

	public LoginMember(Long id, String email, Integer age) {
		super(id, email, age, true);
	}
}