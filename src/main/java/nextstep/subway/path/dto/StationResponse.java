package nextstep.subway.path.dto;

public class StationResponse {
    private final long id;
    private final String name;

    public StationResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
