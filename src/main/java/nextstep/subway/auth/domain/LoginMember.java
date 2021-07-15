package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.AGE;

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
    	return AGE.CHILD.isInRange(age);
	}

	public boolean isAdolescent() {
    	return AGE.ADOLESCENT.isInRange(age);
	}

	public boolean isAdult() {
		return AGE.ADULT.isInRange(age);
	}
}
