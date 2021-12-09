package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.favorite.exception.IllegalFavoriteArgumentException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

import static java.util.Objects.isNull;

@Entity
@Table(indexes = {
        @Index(unique = true, name = "member_id_source_id_target_id_unique_index", columnList = "member_id,source_id,target_id")
})
public class Favorite extends BaseEntity {
    private static final String NOT_FOUND_USER_ERROR_MESSAGE = "즐겨찾기할 유저가 존재하지 않습니다.";
    private static final String NOT_FOUND_SOURCE_ERROR_MESSAGE = "즐겨찾기할 출발역이 존재하지 않습니다.";
    private static final String NOT_FOUND_TARGET_ERROR_MESSAGE = "즐겨찾기할 도착역이 존재하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;

    protected Favorite() {
    }

    private Favorite(Member member, Station source, Station target) {
        validate(member, source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station source, Station target) {
        return new Favorite(member, source, target);
    }

    private void validate(Member member, Station source, Station target) {
        if (isNull(member)) {
            throw new IllegalFavoriteArgumentException(NOT_FOUND_USER_ERROR_MESSAGE);
        }
        if (isNull(source)) {
            throw new IllegalFavoriteArgumentException(NOT_FOUND_SOURCE_ERROR_MESSAGE);
        }
        if (isNull(target)) {
            throw new IllegalFavoriteArgumentException(NOT_FOUND_TARGET_ERROR_MESSAGE);
        }
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

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;

        Favorite favorite = (Favorite) target;

        return id != null ? id.equals(favorite.id) : favorite.id == null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
