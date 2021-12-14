package nextstep.subway.favorite.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station source;

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_station_id", nullable = false)
    private Station target;

    @ManyToOne
    private Member member;

    protected Favorite() {
    }

    private Favorite(Station source, Station target, Member member) {
        validateDuplicateStation(source, target);
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public static Favorite of(Station source, Station target, Member member) {
        return new Favorite(source, target, member);
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

    public Member getMember() {
        return member;
    }

    private void validateDuplicateStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new BadRequestException("상행역과 하행역이 같으면 등록할 수 없습니다.");
        }
    }
}
