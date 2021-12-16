package nextstep.subway.favorite.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "source_id")
	private Station source;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "target_id")
	private Station target;


	protected Favorite() {
	}

	public Favorite(Member member, Station source, Station target) {
		this(null, member, source, target);
	}
	public Favorite(Long id, Member member, Station source, Station target) {
		this.id = id;
		this.member = member;
		this.source = source;
		this.target = target;
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

	@Override
	public boolean equals(Object o) {;
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Favorite favorite = (Favorite)o;
		return Objects.equals(id, favorite.id) && Objects.equals(member, favorite.member)
			&& Objects.equals(source, favorite.source) && Objects.equals(target, favorite.target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, member, source, target);
	}
}
