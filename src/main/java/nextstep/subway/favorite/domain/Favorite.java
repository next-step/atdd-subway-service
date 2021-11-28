package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.FavoriteException;
import nextstep.subway.exception.error.ErrorCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

import static nextstep.subway.utils.ValidationUtils.isNull;

@Entity
public class Favorite extends BaseEntity {

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

    public Favorite(Member member, Station source, Station target) {
        validate(member, source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    private void validate(Member member, Station source, Station target) {
        if (isNull(member)) {
            throw new FavoriteException(ErrorCode.BAD_ARGUMENT, "유저 정보가 없습니다.");
        }
        if (isNull(source)) {
            throw new FavoriteException(ErrorCode.BAD_ARGUMENT, "출발역이 없습니다.");
        }
        if (isNull(target)) {
            throw new FavoriteException(ErrorCode.BAD_ARGUMENT, "도착역이 없습니다.");
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

}
