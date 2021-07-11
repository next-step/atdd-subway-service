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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Station source;
	private Station target;
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	public Favorite(Station source, Station target) {
		this.source = source;
		this.target = target;
	}

	public Long getId() {
		return id;
	}

	public Station getSourceStation() {
		return source;
	}

	public Station getTargetStation() {
		return target;
	}

	public void toMember(Member member) {
		this.member = member;
	}

	public boolean equalsById (Long id) {
		if (this.id.equals(id)) {
			return true;
		}

		return false;
	}
}
