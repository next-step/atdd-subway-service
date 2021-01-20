package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
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

	@ManyToOne
	private Member member;

	@ManyToOne
	private Station sourceStation;

	@ManyToOne
	private Station targetStation;

	protected Favorite() {
	}

	public Favorite(Member member, Station sourceStation, Station targetStation) {
		this.member = member;
		this.sourceStation = sourceStation;
		this.targetStation = targetStation;
	}

	public Long getId() {
		return id;
	}

	public Member getMember() {
		return member;
	}

	public Station getSourceStation() {
		return sourceStation;
	}

	public Station getTargetStation() {
		return targetStation;
	}

	public boolean belongTo(Long memberId) {
		if (this.member == null) {
			return false;
		}
		return this.member.isEqualIdTo(memberId);
	}
}
