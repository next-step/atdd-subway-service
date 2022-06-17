package nextstep.subway.favorite.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Station target;

    protected Favorite() {
    }

    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public boolean isCreateBy(Long memberId) {
        return this.memberId.equals(memberId);
    }
}
