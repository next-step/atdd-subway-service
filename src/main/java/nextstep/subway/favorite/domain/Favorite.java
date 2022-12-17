package nextstep.subway.favorite.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
        checkMemberIsExists(member);
        checkStationIsNotNull(sourceStation);
        checkStationIsNotNull(targetStation);
        return new Favorite(member, sourceStation, targetStation);
    }

    private static void checkMemberIsExists(Member member) {
        if (member == null) {
            throw new IllegalArgumentException(ErrorCode.NO_EXISTS_MEMBER_IN_FAVORITE.getErrorMessage());
        }
    }

    private static void checkStationIsNotNull(Station station) {
        if (station == null) {
            throw new IllegalArgumentException(ErrorCode.NO_EXISTS_STATION_IN_FAVORITE.getErrorMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }
}
