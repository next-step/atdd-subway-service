package nextstep.subway.station.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;

public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
    }

    public static List<StationResponse> of(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public static List<StationResponse> of(Stations stations) {
        return of(stations.getStationElements());
    }

    public static List<StationResponse> of(Path path) {
        return of(path.getStations());
    }

    public StationResponse() {
    }

    public StationResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationResponse that = (StationResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName())
                && Objects.equals(getCreatedDate(), that.getCreatedDate()) && Objects.equals(
                getModifiedDate(), that.getModifiedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCreatedDate(), getModifiedDate());
    }
}
