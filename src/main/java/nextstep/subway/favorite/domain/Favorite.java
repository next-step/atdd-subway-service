package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {}

    private Favorite(Builder builder) {
        this.member = builder.member;
        this.source = builder.source;
        this.target = builder.target;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Member member;
        private Station source;
        private Station target;

        public Builder(){
        }

        public Builder member(Member member) {
            this.member = member;
            return this;
        }

        public Builder source(Station source) {
            this.source = source;
            return this;
        }

        public Builder target(Station target) {
            this.target = target;
            return this;
        }

        public Favorite build() {
            return new Favorite(this);
        }
    }
}
