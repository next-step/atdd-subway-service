package nextstep.subway.member.domain;

import java.util.regex.Pattern;

import javax.persistence.Embeddable;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;

@Embeddable
public class Email {

	public static final Pattern VALIDATE_PATTERN = Pattern.compile("^(.+)@(\\S+)$");

	private String email;

	protected Email() {
	}

	private Email(String email) {
		this.email = email;
	}

	public static Email of(String email) {
		validate(email);
		return new Email(email);
	}

	private static void validate(String email) {
		if (!VALIDATE_PATTERN.matcher(email).matches()) {
			throw new AppException(ErrorCode.WRONG_INPUT, "이메일 양식에 맞지 않습니다");
		}
	}

	public String toText() {
		return this.email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Email email1 = (Email)o;

		return email.equals(email1.email);
	}

	@Override
	public int hashCode() {
		return email.hashCode();
	}
}
