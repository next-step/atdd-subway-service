package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
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

	@JoinColumn(name = "source_station_id")
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Station source;

	@JoinColumn(name = "target_station_id")
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Station target;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Member member;

	protected Favorite() {
	}

	public Favorite(Station source, Station target, Member member) {
		this.source = source;
		this.target = target;
		this.member = member;
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
