package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id")
    private Station target;

    protected Favorite() {
    }

    public Favorite(Member member, Station sourceStation, Station targetStation) {
        this.member = member;
        this.source = sourceStation;
        this.target = targetStation;
    }

    public Favorite(Long id, Member member, Station sourceStation, Station targetStation) {
        this.id = id;
        this.member = member;
        this.source = sourceStation;
        this.target = targetStation;
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

    public boolean isOwner(Long memberId) {
        return this.member.isEquals(memberId);
    }
}
