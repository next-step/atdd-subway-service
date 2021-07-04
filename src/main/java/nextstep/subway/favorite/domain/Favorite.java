package nextstep.subway.favorite.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "favorite_to_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_station_id", foreignKey = @ForeignKey(name = "favorite_to_source_station"))
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id", foreignKey = @ForeignKey(name = "favorite_to_target_station"))
    private Station target;

    public Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public Favorite(Long id, Member member, Station source, Station target) {
        this.id = id;
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
}
