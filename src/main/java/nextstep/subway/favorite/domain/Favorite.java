package nextstep.subway.favorite.domain;

import nextstep.subway.common.ErrorMessage;
import nextstep.subway.common.exception.BadRequestException;
import nextstep.subway.common.exception.CanNotDeleteException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    protected Favorite() {
    }

    public Favorite(Long memberId, Station source, Station target) {
        validateFavorite(source, target);
        this.source = source;
        this.target = target;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public void validateMember(Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new CanNotDeleteException(ErrorMessage.FAVORITE_CAN_NOT_DELETE);
        }
    }

    private void validateFavorite(Station source, Station target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new NotFoundException(ErrorMessage.STATION_NOT_FOUND);
        }

        if (source.equals(target)) {
            throw new BadRequestException(ErrorMessage.SAME_CAN_NOT_SAME);
        }
    }
}
