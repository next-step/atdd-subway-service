package nextstep.subway.favorite.domain;

import static java.util.Objects.*;
import static javax.persistence.FetchType.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "creator_id")
	private Member creator;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "source_station_id")
	private Station source;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "target_station_id")
	private Station target;

	protected Favorite() {}

	private Favorite(Member member, Station source, Station target) {
		validateNotNull(member, source, target);
		validateNotEqual(source, target);
		this.creator = member;
		this.source = source;
		this.target = target;
	}

	public static Favorite of(Member member, Station source, Station target) {
		return new Favorite(member, source, target);
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

	public boolean isCreateBy(Member creator) {
		return this.creator.equals(creator);
	}

	private void validateNotNull(Member member, Station source, Station target) {
		if (isNull(member) || isNull(source) || isNull(target)) {
			throw new IllegalArgumentException();
		}
	}

	private void validateNotEqual(Station source, Station target) {
		if (source.equals(target)) {
			throw new IllegalArgumentException("같은 시작역과 도착역을 즐겨찾기 할 수 없습니다.");
		}
	}
}
