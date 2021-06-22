package nextstep.subway.favorite.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.LineHasNotExistStationException;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station target;

    protected Favorite() {
    }

    public static Favorite create(Lines lines, Member member, Station source, Station target) {
        if (!lines.containsStationsExactly(source, target)) {
            throw new LineHasNotExistStationException();
        }

        return new Favorite(member, source, target);
    }

    private Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
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

    public boolean hasPermission(LoginMember loginMember) {
        return member.getEmail().equals(loginMember.getEmail());
    }
}
