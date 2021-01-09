package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "source_station_id")
	private Station source;

	@ManyToOne
	@JoinColumn(name = "target_station_id")
	private Station target;

	public Favorite() {
	}

	public Favorite(Member member, Station source, Station target) {
		this.member = member;
		this.source = source;
		this.target = target;
		this.member.addFavorite(this);
	}

	public Long getId() {
		return id;
	}

	public Member getMember() {
		return member;
	}

	public Station getSource() {
		return source;
	}

	public Station getTarget() {
		return target;
	}

	public boolean isEquals(Long sourceId, Long targetId) {
		return source.isEquals(sourceId) && target.isEquals(targetId);
	}
}
