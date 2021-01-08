package nextstep.subway.station.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.station.domain.Station;

@AllArgsConstructor
@Getter
public class StationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public static StationResponse of(Station station) {
		return new StationResponse(station.getId(), station.getName(), station.getCreatedDate(),
			station.getModifiedDate());
	}
}
