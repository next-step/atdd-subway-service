package nextstep.subway.Favorite.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

// @Entity
public class Favorite {

	// @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Station source;
	private Station target;
	private Long memberId;

	public static Favorite of(Station source, Station target) {
		return new Favorite();
	}

	public long id() {
		return id;
	}

	public Station source() {
		return null;
	}

	public Station target() {
		return null;
	}

	public Member memberId() {
		return null;
	}
}
