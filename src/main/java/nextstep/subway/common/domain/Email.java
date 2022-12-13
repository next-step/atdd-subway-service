package nextstep.subway.common.domain;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import io.jsonwebtoken.lang.Assert;

@Embeddable
public class Email {
	private static final Pattern EMAIL_PATTERN = Pattern.compile(
		"^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

	@Column(name = "email", unique = true, nullable = false)
	private String value;

	protected Email() {
	}

	private Email(String value) {
		Assert.hasText(value, "이메일 값은 필수입니다.");
		Assert.isTrue(matchEmailFormat(value), String.format("[%s] 이메일 형식이 올바르지 않습니다.", value));
		this.value = value;
	}

	public static Email from(String value) {
		return new Email(value);
	}

	private boolean matchEmailFormat(String value) {
		return EMAIL_PATTERN.matcher(value).matches();
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Email email = (Email)o;
		return Objects.equals(value, email.value);
	}

	@Override
	public String toString() {
		return value;
	}
}
