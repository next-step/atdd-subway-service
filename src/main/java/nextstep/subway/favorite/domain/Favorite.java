package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
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

	@ManyToOne
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_favorite_to_member"))
	private Member member;

	@ManyToOne
	@JoinColumn(name = "source_id", foreignKey = @ForeignKey(name = "fk_favorite_to_source"))
	private Station source;

	@ManyToOne
	@JoinColumn(name = "target_id", foreignKey = @ForeignKey(name = "fk_favorite_to_target"))
	private Station target;

	protected Favorite() {

	}

	public static Favorite of(Member member, Station source, Station target) {
		Favorite favorite = new Favorite();
		favorite.member = member;
		favorite.source = source;
		favorite.target = target;
		return favorite;
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
}
