package nextstep.subway.favorite.domain;


import nextstep.subway.BaseEntity;
import nextstep.subway.exception.Message;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        validateStations(source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    private void validateStations(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(Message.ERROR_START_AND_END_STATIONS_ARE_SAME.showText());
        }
    }

    public Favorite(Long id, Member member, Station source, Station target) {
        validateStations(source, target);
        this.member = member;
        this.source = source;
        this.target = target;
        this.id = id;
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
