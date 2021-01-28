package nextstep.subway.line.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private int additionalFare;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations, int additionalFare,
        LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.additionalFare = additionalFare;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse of(Line line) {
        return Builder.LineResponse()
            .id(line.getId())
            .name(line.getName())
            .color(line.getColor())
            .stations(getStationResponse(line))
            .additionalFare(line.getAdditionalFee().getFare())
            .createdDate(line.getCreatedDate())
            .modifiedDate(line.getModifiedDate())
            .build();
    }

    private static List<StationResponse> getStationResponse(final Line line) {
        return line.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getAdditionalFare() { return additionalFare; }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public static final class Builder {
        private Long id;
        private String name;
        private String color;
        private List<StationResponse> stations;
        private int additionalFare;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        private Builder() {
        }

        public static Builder LineResponse() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder stations(List<StationResponse> stations) {
            this.stations = stations;
            return this;
        }

        public Builder additionalFare(int additionalFare) {
            this.additionalFare = additionalFare;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder modifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public LineResponse build() {
            return new LineResponse(id, name, color, stations, additionalFare, createdDate, modifiedDate);
        }
    }

}
