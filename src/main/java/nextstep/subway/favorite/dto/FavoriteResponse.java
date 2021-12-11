package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private Long id;
    private Long sourceId;
    private Long targetId;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long sourceId, Long targetId) {
        this.id = id;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSource().getId(), favorite.getTarget().getId());
    }

    public static List<FavoriteResponse> listOf(List<Favorite> members) {
        return members.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
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
