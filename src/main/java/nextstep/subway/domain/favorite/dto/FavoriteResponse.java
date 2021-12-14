package nextstep.subway.domain.favorite.dto;

import nextstep.subway.domain.favorite.domain.Favorite;
import nextstep.subway.domain.station.domain.Station;

import java.time.LocalDateTime;

public class FavoriteResponse {

    private Long id;
    private FavoriteStation source;
    private FavoriteStation target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id) {
        this.id = id;
    }

    public FavoriteResponse(Long id, FavoriteStation source, FavoriteStation target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public FavoriteStation getSource() {
        return source;
    }

    public FavoriteStation getTarget() {
        return target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), FavoriteStation.of(favorite.getSource()), FavoriteStation.of(favorite.getTarget()));
    }

    public static class FavoriteStation {
        private Long id;
        private String name;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public FavoriteStation() {
        }

        public FavoriteStation(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
            this.id = id;
            this.name = name;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
        }

        public Long getId() {
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

        public static FavoriteStation of(Station station) {
            return new FavoriteStation(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
        }
    }
}
