package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PathStationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdAt;


	public static PathStationResponse of(Station station) {
		return new PathStationResponse(station.getId(), station.getName(), station.getCreatedDate());
	}

	public PathStationResponse() {
	}

	public static List<PathStationResponse> of(List<Station> stations) {
		return stations.stream().map(PathStationResponse::of).collect(Collectors.toList());
	}

	public PathStationResponse(Long id, String name, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

}
