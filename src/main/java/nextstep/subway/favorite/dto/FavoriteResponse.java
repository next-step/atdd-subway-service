package nextstep.subway.favorite.dto;

import java.time.LocalDateTime;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

public class FavoriteResponse {
    private final long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoriteResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        StationResponse source = StationResponse.from(favorite.getSource());
        StationResponse target = StationResponse.from(favorite.getTarget());
        return new FavoriteResponse(favorite.getId(), source, target);
    }

    public long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    private static class StationResponse {

        private final long id;
        private final String name;
        private final LocalDateTime createdDate;
        private final LocalDateTime modifiedDate;

        public StationResponse(long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
            this.id = id;
            this.name = name;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
        }

        public static StationResponse from(Station station) {
            return new StationResponse(station.getId(), station.getName(), station.getCreatedDate(),
                    station.getModifiedDate());
        }

        public long getId() {
            return id;
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
