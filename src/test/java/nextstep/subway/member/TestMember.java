package nextstep.subway.member;

public enum TestMember {
	윤준석("y2o2u2n@gmail.com", "password", 28);

	private String email;
	private String password;
	private int age;

	TestMember(String email, String password, Integer age) {
		this.email = email;
		this.password = password;
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public int getAge() {
		return age;
	}
}
