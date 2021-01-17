package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Favorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Station source;

	@OneToOne
	private Station target;

	@ManyToOne
	private Member member;

	protected Favorite() {
	}

	public Favorite(List<Station> stations, Member member) {
		if(stations.size() != 2){
			throw new IllegalArgumentException("즐겨찾을 역이 정상적으로 조회되지 않았습니다.");
		}
		this.source = stations.get(0);
		this.target = stations.get(1);
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
}
