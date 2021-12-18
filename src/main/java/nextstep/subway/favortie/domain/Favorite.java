package nextstep.subway.favortie.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	@JoinColumn
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@JoinColumn
	@ManyToOne(fetch = FetchType.LAZY)
	private Station source;

	@JoinColumn
	@ManyToOne(fetch = FetchType.LAZY)
	private Station target;

	protected Favorite() {
	}

	private Favorite(Long id, Member member, Station source, Station target) {
		this.id = id;
		this.member = member;
		this.source = source;
		this.target = target;
	}

	public static Favorite of(Long id, Member member, Station source, Station target) {
		return new Favorite(id, member, source, target);
	}

	public static Favorite of(Member member, Station source, Station target) {
		return new Favorite(null, member, source, target);
	}

	public boolean isSameMember(Member other) {
		return this.member.equals(other);
	}

	public boolean isSamePath(Station source, Station target) {
		return this.source.equals(source) && this.target.equals(target);
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Favorite favorite = (Favorite)o;

		return id.equals(favorite.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
