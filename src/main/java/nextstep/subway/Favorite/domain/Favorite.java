package nextstep.subway.Favorite.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.jsonwebtoken.lang.Assert;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "source_station_id", foreignKey = @ForeignKey(name = "fk_favorite_to_source_station"))
	private Station source;

	@ManyToOne(optional = false)
	@JoinColumn(name = "target_station_id", foreignKey = @ForeignKey(name = "fk_favorite_to_target_station"))
	private Station target;

	@Column
	private long memberId;

	protected Favorite() {
	}

	private Favorite(Station source, Station target, long memberId) {
		validate(source, target);
		this.source = source;
		this.target = target;
		this.memberId = memberId;
	}

	public static Favorite of(Station source, Station target, long memberId) {
		return new Favorite(source, target, memberId);
	}

	public Station source() {
		return source;
	}

	public Station target() {
		return target;
	}

	public long memberId() {
		return memberId;
	}

	private void validate(Station source, Station target) {
		Assert.notNull(source, "출발역은 필수입니다.");
		Assert.notNull(target, "도착역은 필수입니다.");
		if (source.equals(target)) {
			throw new IllegalArgumentException(String.format("출발역(%s) 도착역(%s)은 같을 수 없습니다.", source, target));
		}
	}

}
