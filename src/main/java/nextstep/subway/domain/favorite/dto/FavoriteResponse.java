package nextstep.subway.domain.favorite.dto;

public class FavoriteResponse {

    private Long id;
    private Station source;
    private Station target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id) {
        this.id = id;
    }

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
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

    public static class Station {
        private Long id;
        private String name;
        private String createdDate;
        private String modifiedDate;

        public Station() {
        }

        public Station(Long id, String name, String createdDate, String modifiedDate) {
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

        public String getCreatedDate() {
            return createdDate;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }
    }
}
