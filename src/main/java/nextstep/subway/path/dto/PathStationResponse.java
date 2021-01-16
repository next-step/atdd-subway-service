package nextstep.subway.path.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class PathStationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdAt;

	public PathStationResponse(Long id, String name, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public static PathStationResponse of(Station station) {
		return new PathStationResponse(station.getId(), station.getName(), station.getCreatedDate());
	}

	public PathStationResponse() {
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PathStationResponse that = (PathStationResponse)o;
		return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName())
			&& Objects.equals(getCreatedAt(), that.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName(), getCreatedAt());
	}
}
