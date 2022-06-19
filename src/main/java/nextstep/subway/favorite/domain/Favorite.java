package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "member_source_target_udx", columnList = "member_id,source_id,target_id", unique = true) })
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Station source;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Station target;

    protected Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public Station getTarget() {
        return this.target;
    }

    public Station getSource() {
        return this.source;
    }

    public Long getId() {
        return this.id;
    }
}
