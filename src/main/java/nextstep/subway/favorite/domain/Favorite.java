package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"member_id", "source_id", "target_id"}
		)
	}
)
public class Favorite {
	public static final String SAME_SOURCE_AND_TARGET = "출발역과 도착역이 같습니다.";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "source_id")
	private Station source;

	@ManyToOne
	@JoinColumn(name = "target_id")
	private Station target;

	protected Favorite() {
	}

	public Favorite(Member member, Station source, Station target) {
		validate(source, target);
		this.member = member;
		this.source = source;
		this.target = target;
	}

	private void validate(Station source, Station target) {
		if (source.equals(target)) {
			throw new IllegalArgumentException(SAME_SOURCE_AND_TARGET);
		}
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
