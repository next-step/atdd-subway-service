package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;

public class PathStationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdAt;

	private PathStationResponse() {
	}

	public PathStationResponse(Long id, String name, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public static PathStationResponse from(Station station) {
		return new PathStationResponse(station.getId(), station.getName(), station.getCreatedDate());
	}

	public static List<PathStationResponse> newList(List<Station> station) {
		return station.stream()
			.map(PathStationResponse::from)
			.collect(Collectors.toList());
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
