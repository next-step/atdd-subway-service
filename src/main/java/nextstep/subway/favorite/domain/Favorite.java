package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Member owner;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {
    }

    public Favorite(Member owner, Station source, Station target) {
        this.owner = owner;
        this.source = source;
        this.target = target;
    }

    public boolean isNotOwner(Long ownerId) {
        return !owner.getId().equals(ownerId);
    }

    public Long getId() {
        return id;
    }

    public Member getOwner() {
        return owner;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
