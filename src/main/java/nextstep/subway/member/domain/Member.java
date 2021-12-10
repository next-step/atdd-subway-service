package nextstep.subway.member.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import nextstep.subway.BaseEntity;
import nextstep.subway.auth.exception.AuthorizationException;
import nextstep.subway.common.ErrorCode;

@Entity
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String password;

	@Embedded
	private Age age;

	protected Member() {
	}

	private Member(String email, String password, Age age) {
		this.email = email;
		this.password = password;
		this.age = age;
	}

	public static Member from(String email, String password, Integer age) {
		return new Member(email, password, Age.from(age));
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Age getAge() {
		return age;
	}

	public Integer getIntAge() {
		return age.getAge();
	}

	public void update(Member member) {
		this.email = member.email;
		this.password = member.password;
		this.age = member.age;
	}

	public void checkPassword(String password) {
		if (!StringUtils.equals(this.password, password)) {
			throw new AuthorizationException(ErrorCode.INCORRECT_PASSWORD);
		}
	}
}
