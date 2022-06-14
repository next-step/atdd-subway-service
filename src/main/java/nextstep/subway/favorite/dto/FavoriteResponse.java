package nextstep.subway.favorite.dto;

import java.time.LocalDateTime;

public class FavoriteResponse {
    private Long id;
    private Long sourceId;
    private Long targetId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public FavoriteResponse(Long id, Long sourceId, Long targetId, LocalDateTime createdDate,
                            LocalDateTime modifiedDate) {
        this.id = id;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
