package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "source_station_id")
    private Station source;

    @OneToOne
    @JoinColumn(name = "target_station_id")
    private Station target;

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    protected Favorite() {
    }

    public Long id() {
        return id;
    }

    public Member member() {
        return member;
    }

    public Station source() {
        return source;
    }

    public Station target() {
        return target;
    }

    public void deleteFavorite() {
        member.deleteFavorite(this);
    }
}
