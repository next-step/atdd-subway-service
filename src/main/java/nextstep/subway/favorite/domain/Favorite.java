package nextstep.subway.favorite.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.BaseEntity;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

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

    protected Favorite() {
    }

    private Favorite(Station source, Station target, Member member) {
        validateFavorite(source, target);
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public static Favorite of(Station source, Station target, Member member) {
        return new Favorite(source, target, member);
    }

    private void validateFavorite(Station source, Station target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new NotFoundException(ExceptionType.NOT_FOUND_STATION);
        }

        if (source == target) {
            throw new BadRequestException(ExceptionType.CAN_NOT_SAME_STATION);
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
