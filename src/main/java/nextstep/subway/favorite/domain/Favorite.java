package nextstep.subway.favorite.domain;

import static java.util.Objects.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    Station target;

    protected Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        checkArguments(member, source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    private void checkArguments(Member member, Station source, Station target) {
        if (isNull(member)) {
            throw new IllegalArgumentException("즐겨찾기를 추가할 사용자가 없습니다.");
        }

        if (isNull(source) || isNull(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 모두 존재해야 합니다.");
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
