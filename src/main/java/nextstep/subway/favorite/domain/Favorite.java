package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sourceId;
    private Long targetId;

    protected Favorite() {
    }

    public Favorite(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Favorite(Long id, Long sourceId, Long targetId) {
        this.id = id;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getId() {
        return id;
    }
}
