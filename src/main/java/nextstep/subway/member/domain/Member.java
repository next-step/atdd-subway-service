package nextstep.subway.member.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.auth.application.AuthorizationException;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String password;
	private Integer age;

	public Member(String email, String password, Integer age) {
		this.email = email;
		this.password = password;
		this.age = age;
	}
	
	public void update(Member member) {
		this.email = member.email;
		this.password = member.password;
		this.age = member.age;
	}

	public void checkPassword(String password) {
		if (!StringUtils.equals(this.password, password)) {
			throw new AuthorizationException("비밀번호가 틀렸습니다.");
		}
	}
}
