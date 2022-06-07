package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import nextstep.subway.BaseEntity;

@Entity
@Table(name = "favorite")
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Favorite() {}

    public Favorite(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }
}
