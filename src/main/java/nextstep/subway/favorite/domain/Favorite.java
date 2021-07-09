package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sourceStationId")
	private Station source;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "targetStationId")
	private Station target;

	protected Favorite() {
	}

	public Favorite(Station source, Station target, Long memberId) {
		this.source = source;
		this.target = target;
		this.memberId = memberId;
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
}
