package nextstep.subway.favorite.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/18
 * @description :
 **/
@Entity
public class Favorite extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne
	@JoinColumn(name = "source_station_id")
	private Station source;

	@ManyToOne
	@JoinColumn(name = "target_station_id")
	private Station target;

	public Favorite() {
	}

	public Favorite(Long id, Member member, Station source, Station target) {
		this.id = id;
		this.member = member;
		this.source = source;
		this.target = target;
	}

	public Favorite(Member member, Station source, Station target) {
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Favorite favorite = (Favorite)o;
		return Objects.equals(getId(), favorite.getId()) && Objects.equals(getMember(),
			favorite.getMember()) && Objects.equals(getSource(), favorite.getSource())
			&& Objects.equals(getTarget(), favorite.getTarget());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getMember(), getSource(), getTarget());
	}

	@Override
	public String toString() {
		return "Favorite{" +
			"id=" + id +
			", member=" + member +
			", source=" + source +
			", target=" + target +
			'}';
	}
}
