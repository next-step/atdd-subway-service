package nextstep.subway.auth.domain;

import java.util.Objects;

import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Email;

public class LoginMember {
	private Long id;
	private Email email;
	private Age age;

	public LoginMember() {
	}

	public LoginMember(Long id, Email email, Age age) {
		this.id = id;
		this.email = email;
		this.age = age;
	}

	public Long getId() {
		return id;
	}

	public Email getEmail() {
		return email;
	}

	public Age getAge() {
		return age;
	}

	public boolean isNull() {
		return Objects.isNull(id);
	}
}
