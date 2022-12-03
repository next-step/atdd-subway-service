package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "target_station_id", nullable = false)
    private Station targetStation;

    protected Favorite() {
    }

    private Favorite(Member member, Station sourceStation, Station targetStation) {
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static Favorite of(Member member, Station sourceStation, Station targetStation) {
        checkMemberNotNull(member);
        checkStationNotNull(sourceStation);
        checkStationNotNull(targetStation);
        return new Favorite(member, sourceStation, targetStation);
    }
    private static void checkMemberNotNull(Member member) {
        if (member == null) {
            throw new EntityNotFoundException(ErrorEnum.NOT_EXISTS_MEMBER.message());
        }
    }

    private static void checkStationNotNull(Station station) {
        if (station == null) {
            throw new EntityNotFoundException(ErrorEnum.NOT_EXISTS_STATION.message());
        }
    }
    public Long getId() {
        return id;
    }
}
