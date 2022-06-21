package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {
    private static final String NOT_NULL_MEMBER = "사용자가 없으면 즐겨찾기를 등록할 수 없습니다.";
    private static final String NOT_NULL_SOURCE = "출발역이 없으면 즐겨찾기를 등록할 수 없습니다.";
    private static final String NOT_NULL_TARGET = "도착역이 없으면 즐겨찾기를 등록할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;
    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {}

    private Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station source, Station target) {
        validateNullObjects(member, source, target);
        return new Favorite(member, source, target);
    }

    private static void validateNullObjects(Member member, Station source, Station target) {
        if (Objects.isNull(member)) {
            throw new IllegalArgumentException(NOT_NULL_MEMBER);
        }
        if (Objects.isNull(source)) {
            throw new IllegalArgumentException(NOT_NULL_SOURCE);
        }
        if (Objects.isNull(target)) {
            throw new IllegalArgumentException(NOT_NULL_TARGET);
        }
    }
}
