package nextstep.subway.auth.domain;

public class Member {
	private Long id;
	private String email;
	private Integer age;
	private boolean isLoggedIn;

	public Member(Long id, String email, Integer age, boolean isLoggedIn) {
		this.id = id;
		this.email = email;
		this.age = age;
		this.isLoggedIn = isLoggedIn;
	}

	public Member(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
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

	public boolean isLoggedIn() {
		return isLoggedIn;
	}
}
