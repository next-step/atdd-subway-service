package nextstep.subway.station.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

public class StationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	public static StationResponse of(Station station) {
		return new StationResponse(station.getId(), station.getName(), station.getCreatedDate(),
			station.getModifiedDate());
	}

	public static List<StationResponse> of(Stations stations) {
		return stations.getStations()
			.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
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
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		StationResponse that = (StationResponse)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
