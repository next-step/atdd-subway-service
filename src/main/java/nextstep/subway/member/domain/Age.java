package nextstep.subway.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nextstep.subway.path.domain.AgePriceRate;

@Embeddable
public class Age {

	@Column
	private Integer age;

	protected Age() {
	}

	private Age(Integer age) {
		this.age = age;
	}

	public static Age from(Integer age) {
		return new Age(age);
	}

	public boolean isTeenager() {
		return age > AgePriceRate.TEENAGER.getMinAge() && age < AgePriceRate.TEENAGER.getMaxAge();
	}

	public boolean isChild() {
		return age > AgePriceRate.CHILD.getMinAge() && age < AgePriceRate.CHILD.getMaxAge();
	}

	public Integer getAge() {
		return age;
	}
}
