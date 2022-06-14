package nextstep.subway.line.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private int extraCharge;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public LineResponse() {
    }

    private LineResponse(Long id, String name, String color, List<StationResponse> stations, int extraCharge, LocalDateTime createdDate,
                        LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.extraCharge = extraCharge;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                StationResponse.of(line.getStations()),
                line.getExtraCharge(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
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

    public int getExtraCharge() {
        return extraCharge;
    }

    public List<StationResponse> getStations() {
        return stations;
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
        LineResponse that = (LineResponse) o;
        return name.equals(that.name) && color.equals(that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
