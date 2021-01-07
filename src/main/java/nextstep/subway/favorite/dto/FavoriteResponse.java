package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    private FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(final Favorite favorite) {
        StationResponse source = StationResponse.of(favorite.getSourceStation());
        StationResponse target = StationResponse.of(favorite.getTargetStation());
        return new FavoriteResponse(favorite.getId(), source, target);
    }


    public static List<FavoriteResponse> ofList(final List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public static class StationResponse {

        private String name;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public StationResponse() {
        }

        private StationResponse(final String name, final LocalDateTime createdDate, final LocalDateTime modifiedDate) {
            this.name = name;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
        }

        public static StationResponse of(Station station) {
            return new StationResponse(station.getName(), station.getCreatedDate(), station.getModifiedDate());
        }

        public String getName() {
            return name;
        }

        public LocalDateTime getCreatedDate() {
            return createdDate;
        }

        public LocalDateTime getModifiedDate() {
            return modifiedDate;
        }
    }
}
