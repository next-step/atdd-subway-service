package nextstep.subway.favorite.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.BaseEntity;
import nextstep.subway.common.ErrorCode;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "source")
	private Station source;

	@ManyToOne
	@JoinColumn(name = "target")
	private Station target;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	protected Favorite() {
	}

	private Favorite(Station source, Station target, Member member) {
		this.source = source;
		this.target = target;
		this.member = member;
	}

	public static Favorite of(Station source, Station target, Member member) {
		return new Favorite(source, target, member);
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

	public boolean isOwner(Member member) {
		return this.member.getId().equals(member.getId());
	}

	public void validOwner(Member expectMember) {
		if (!isOwner(expectMember)) {
			throw new FavoriteException(ErrorCode.VALID_CAN_NOT_REMOVE_FAVORITE);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Favorite favorite = (Favorite)o;
		return Objects.equals(id, favorite.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
