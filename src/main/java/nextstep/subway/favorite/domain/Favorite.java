package nextstep.subway.favorite.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Favorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne
	@JoinColumn(name = "source_station_id", nullable = false)
	private Station sourceStation;

	@ManyToOne
	@JoinColumn(name = "target_station_id", nullable = false)
	private Station targetStation;

	protected Favorite() {
	}

	protected Favorite(Member member, Station sourceStation, Station targetStation) {
		this.member = member;
		this.sourceStation = sourceStation;
		this.targetStation = targetStation;
	}

	public static Favorite of(Member member, Station sourceStation, Station targetStation) {
		return new Favorite(member, sourceStation, targetStation);
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

	public boolean isCreatedBy(Long memberId) {
		return this.member.getId().equals(memberId);
	}
}
