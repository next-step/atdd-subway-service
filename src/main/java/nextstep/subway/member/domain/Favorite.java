package nextstep.subway.member.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.member.application.exception.FavoriteErrorCode;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long stationSourceId;

    @Column(nullable = false)
    private Long stationTargetId;

    protected Favorite() {
    }

    public Favorite(Long stationSourceId, Long stationTargetId) {
        valid(stationSourceId, stationTargetId);
        this.stationSourceId = stationSourceId;
        this.stationTargetId = stationTargetId;
    }

    public Long getId() {
        return id;
    }

    public Long getStationSourceId() {
        return stationSourceId;
    }

    public Long getStationTargetId() {
        return stationTargetId;
    }

    public static Favorite of(long stationSourceId, long stationTargetId) {
        return new Favorite(stationSourceId, stationTargetId);
    }

    public boolean isDuplicate(Favorite favorite) {
        return Objects.equals(stationSourceId, favorite.stationSourceId)
            && Objects.equals(stationTargetId, favorite.stationTargetId);
    }

    public boolean isSameId(Long favoriteId) {
        return Objects.equals(this.id, favoriteId);
    }

    public List<Long> getStationIds() {
        return Arrays.asList(stationSourceId, stationTargetId);
    }

    private void valid(Long stationSourceId, Long stationTargetId) {
        if (Objects.isNull(stationSourceId) || Objects.isNull(stationTargetId)) {
            throw InvalidParameterException.of(FavoriteErrorCode.NOT_EMPTY);
        }

        if (Objects.equals(stationSourceId, stationTargetId)) {
            throw InvalidParameterException.of(FavoriteErrorCode.SOURCE_TARGET_NOT_SAME);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Favorite favorite = (Favorite) o;

        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
