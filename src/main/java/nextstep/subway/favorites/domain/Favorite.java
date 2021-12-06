package nextstep.subway.favorites.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

/**
 * packageName : nextstep.subway.favorites.domain
 * fileName : Favorite
 * author : haedoang
 * date : 2021/12/06
 * description :
 */
@Entity
public class Favorite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "SOURCE_STATION_ID")
    private Station sourceStation;

    @OneToOne
    @JoinColumn(name = "TARGET_STATION_ID")
    private Station targetStation;

    @Embedded
    private Distance distance;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public Favorite(Station sourceStation, Station targetStation, Distance distance) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
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

    public Distance getDistance() {
        return distance;
    }

    public Member getMember() {
        return member;
    }

    public void setSourceStation(Station sourceStation) {
        this.sourceStation = sourceStation;
    }

    public void setTargetStation(Station targetStation) {
        this.targetStation = targetStation;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
    public Favorite by(Member member) {
        this.member = member;
        return this;
    }
}
