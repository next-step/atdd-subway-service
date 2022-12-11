package nextstep.subway.favorite.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.common.exception.InvalidDataException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Favorite extends BaseEntity {

    private static final String IDENTICAL_STATION_EXCEPTION = "출발역과 도착역이 동일합니다.";
    private static final String FAVORITE_ALREADY_EXITS_EXCEPTION = "해당 구간은 사전에 등록되었습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sourceId;
    private Long targetId;

    public Favorite() {
    }

    public Favorite(Long sourceId, Long targetId) {
        validateIdenticalStation(sourceId, targetId);
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    private void validateIdenticalStation(Long sourceId, Long targetId) {
        if (sourceId == targetId) {
            throw new InvalidDataException(IDENTICAL_STATION_EXCEPTION);
        }
    }

    public void validateDuplicate(List<Favorite> favorites) {

        boolean isDuplicated = favorites.stream()
                .anyMatch(favorite -> this.sourceId == favorite.getSourceId() && this.targetId == favorite.getTargetId());

        if (isDuplicated) {
            throw new InvalidDataException(FAVORITE_ALREADY_EXITS_EXCEPTION);
        }
    }
    public Long getId() {
        return id;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
