package nextstep.subway.favorite.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
@NoArgsConstructor
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
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        checkArgument(!sourceStation.equals(targetStation), "출발역과 도착역은 같을 수 없습니다.");

        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }
}
