package nextstep.subway.favorite.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class FavoritePath {
    private static final String REQUIRED_VALUE_ERROR_MESSAGE = "출박역, 도착역, 회원 정보는 필수값 입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Station target;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected FavoritePath() {}

    private FavoritePath(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public static FavoritePath of(Station source, Station target, Member member) {
        validateFavoritePath(source, target, member);
        return new FavoritePath(source, target, member);
    }

    private static void validateFavoritePath(Station source, Station target, Member member) {
        if (Objects.isNull(source) || Objects.isNull(target) || Objects.isNull(member)) {
            throw new IllegalArgumentException(REQUIRED_VALUE_ERROR_MESSAGE);
        }
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
