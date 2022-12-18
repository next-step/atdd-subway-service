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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id")
    private Station target;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    public Favorite() {}

    public Favorite(Long id, Station source, Station target, Member member) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public Long getId() {
        return this.id;
    }

    public void setSource(Station station) {
        this.source = station;
    }

    public Station getSource() {
        return this.source;
    }

    public void setTarget(Station station) {
        this.target = station;
    }

    public Station getTarget() {
        return this.target;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
