package nextstep.subway.favorites.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorites extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "member_id",
        foreignKey = @ForeignKey(name = "FK_FAVORITES_TO_MEMBER"),
        nullable = false
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "source_id",
        foreignKey = @ForeignKey(name = "FK_FAVORITES_TO_SOURCE"),
        nullable = false
    )
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "target_id",
        foreignKey = @ForeignKey(name = "FK_FAVORITES_TO_TARGET"),
        nullable = false
    )
    private Station target;

    protected Favorites() {

    }

    public Favorites(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
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
}
