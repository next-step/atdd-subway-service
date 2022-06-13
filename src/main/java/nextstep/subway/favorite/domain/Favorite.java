package nextstep.subway.favorite.domain;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {
    }

    public Favorite(FavoriteBuilder favoriteBuilder) {
        this.id = favoriteBuilder.id;
        this.member = favoriteBuilder.member;
        this.source = favoriteBuilder.source;
        this.target = favoriteBuilder.target;
    }

    public static FavoriteBuilder builder(Member member, Station source, Station target) {
        return new FavoriteBuilder(member, source, target);
    }

    public static class FavoriteBuilder {
        private Long id;
        private final Member member;
        private final Station source;
        private final Station target;

        private FavoriteBuilder(Member member, Station source, Station target) {
            validateParameter(member, source, target);
            this.member = member;
            this.source = source;
            this.target = target;
        }

        private void validateParameter(Member member, Station source, Station target) {
            validateMemberNotNull(member);
            validateSourceNotNull(source);
            validateTargetNotNull(target);
        }

        private void validateMemberNotNull(Member member) {
            if (Objects.isNull(member)) {
                throw new NotFoundException("회원 정보가 없습니다.");
            }
        }

        private void validateSourceNotNull(Station source) {
            if (Objects.isNull(source)) {
                throw new NotFoundException("출발역 정보가 없습니다.");
            }
        }

        private void validateTargetNotNull(Station target) {
            if (Objects.isNull(target)) {
                throw new NotFoundException("도착역 정보가 없습니다.");
            }
        }

        public FavoriteBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public Favorite build() {
            return new Favorite(this);
        }
    }

    public Long id() {
        return id;
    }

    public Member member() {
        return member;
    }

    public Station source() {
        return source;
    }

    public Station target() {
        return target;
    }
}
