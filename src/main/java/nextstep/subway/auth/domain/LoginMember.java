package nextstep.subway.auth.domain;

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

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

	public boolean isChild() {
    	if (age >= 6 && age < 13) {
			return true;
		}

		return false;
	}

	public boolean isAdolescent() {
		if (age >= 13 && age < 19) {
			return true;
		}

		return false;
	}

	public boolean isAdult() {
		if (age >= 19) {
			return true;
		}

		return false;
	}
}
