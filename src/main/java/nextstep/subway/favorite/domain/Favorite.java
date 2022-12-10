package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.NotFoundDataException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

import static nextstep.subway.exception.type.NotFoundDataExceptionType.NOT_FOUND_STATION;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Favorite() {}

    private Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public static Favorite of(Station sourceStation, Station targetStation, Member member) {
        validationCheckIsExistStation(sourceStation, targetStation);
        
        return new Favorite(sourceStation, targetStation, member);
    }

    private static void validationCheckIsExistStation(Station sourceStation, Station targetStation) {
        if (Objects.isNull(sourceStation)) {
            throw new NotFoundDataException(NOT_FOUND_STATION.getMessage());
        }

        if (Objects.isNull(targetStation)) {
            throw new NotFoundDataException(NOT_FOUND_STATION.getMessage());
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

    public Member getMember() {
        return member;
    }
}
