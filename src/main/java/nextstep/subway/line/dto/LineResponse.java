package nextstep.subway.line.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.dto.StationResponse;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private List<StationResponse> stations;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	private LineResponse(Long id, String name, String color, List<StationResponse> stations, LocalDateTime createdDate,
		LocalDateTime modifiedDate) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.stations = stations;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public static LineResponse of(Line line) {
		if (line == null) {
			throw new NotFoundException("노선 정보를 찾을 수 없습니다.");
		}
		List<StationResponse> stations = convertStationToStationResponse(line);
		return new LineResponse(line.getId(), line.getName(), line.getColor(), stations, line.getCreatedDate(),
			line.getModifiedDate());
	}

	private static List<StationResponse> convertStationToStationResponse(Line line) {
		return line.getStations()
			.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}

}
