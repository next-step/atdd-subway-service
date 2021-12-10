package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

public class LoginMember {

	private Long id;
	private String email;
	private Age age;

	public LoginMember() {
	}

	public LoginMember(Long id, String email, Age age) {
		this.id = id;
		this.email = email;
		this.age = age;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public Age getAge() {
		return age;
	}

	public int getIntAge() {
		return age.getAge();
	}

	public boolean isNull() {
		return this.id == null;
	}

	public boolean isGuest() {
		return false;
	}

	public static class Guest extends LoginMember {
		@Override
		public boolean isGuest() {
			return true;
		}
	}
}
