package nextstep.subway.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected Favorite() {
    }

    private Favorite(Favorite.Builder builder) {
        this.id = builder.id;
        this.source = builder.source;
        this.target = builder.target;
        this.member = builder.member;
    }

    public Favorite(Station source, Station target, Member member) {
        this(null, source, target, member);
    }

    public Favorite(Long id, Station source, Station target, Member member) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.member = member;
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

    public static class Builder {
        private final Long id;
        private final Station source;
        private final Station target;
        private final Member member;

        public Builder() {
            this(null, null, null);
        }

        private Builder(Station source, Station target, Member member) {
            this(null, source, target, null);
        }

        private Builder(Long id, Station source, Station target, Member member) {
            this.id = id;
            this.source = source;
            this.target = target;
            this.member = member;
        }

        public Favorite.Builder id(Long id) {
            return new Favorite.Builder(id, source, target, member);
        }

        public Favorite.Builder source(Station source) {
            return new Favorite.Builder(id, source, target, member);
        }

        public Favorite.Builder target(Station target) {
            return new Favorite.Builder(id, source, target, member);
        }

        public Favorite.Builder member(Member member) {
            return new Favorite.Builder(id, source, target, member);
        }

        public Favorite build() {
            return new Favorite(this);
        }
    }
}
