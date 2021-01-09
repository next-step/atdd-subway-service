package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long memberId;
    private long source;
    private long target;

    protected Favorite() {}

    public Favorite(long memberId, long source, long target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }
}
