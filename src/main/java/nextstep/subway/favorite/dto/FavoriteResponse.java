package nextstep.subway.favorite.dto;

import java.time.LocalDateTime;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
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

        public StationResponse(final String name, final LocalDateTime createdDate, final LocalDateTime modifiedDate) {
            this.name = name;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
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
