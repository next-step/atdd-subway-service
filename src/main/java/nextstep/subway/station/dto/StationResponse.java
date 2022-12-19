package nextstep.subway.station.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;

public class StationResponse {

    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public StationResponse() {
    }

    private StationResponse(
        Long id,
        String name,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
    ) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(
            station.getId(),
            station.getName(),
            station.getCreatedDate(),
            station.getModifiedDate());
    }

    public static List<StationResponse> of(List<Station> stations) {
        return Collections.unmodifiableList(stations.stream()
            .map(StationResponse::from)
            .collect(Collectors.toList()));
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
}
