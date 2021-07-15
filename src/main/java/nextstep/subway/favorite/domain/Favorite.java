package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Station source;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Station target;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Member member;

	protected Favorite() {

	}

	public Long getId() {
		return id;
	}

	public Station getSource() {
		return source;
	}

	public Station getTarget() {
		return target;
	}

	public Member getMember() {
		return member;
	}


}
