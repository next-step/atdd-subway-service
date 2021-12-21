package nextstep.subway.auth.domain;

import java.util.Objects;

import nextstep.subway.member.domain.Member;

public class LoginMember {
	private Long id;
	private String email;
	private Integer age;

	public LoginMember() {
	}

	public LoginMember(Long id, String email, Integer age) {
		this.id = id;
		this.email = email;
		this.age = age;
	}

	public LoginMember(Member member) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.age = member.getAge();
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public Integer getAge() {
		return age;
	}

	public boolean checkAge(int minAge, int maxAge) {
		return age >= minAge && age < maxAge;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		LoginMember that = (LoginMember)o;
		return Objects.equals(id, that.id) && Objects.equals(email, that.email)
			&& Objects.equals(age, that.age);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, age);
	}


}
