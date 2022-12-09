package nextstep.subway.path.dto;

import java.time.LocalDateTime;

import nextstep.subway.station.domain.Station;

public class PathStationResponse {

	private final Long id;
	private final String name;
	private final LocalDateTime createdAt;

	private PathStationResponse(Long id, String name, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public static PathStationResponse of(Long id, String name, LocalDateTime createdAt) {
		return new PathStationResponse(id, name, createdAt);
	}

	public static PathStationResponse from(Station station) {
		return new PathStationResponse(
			station.getId(),
			station.name().toString(),
			station.getCreatedDate());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
