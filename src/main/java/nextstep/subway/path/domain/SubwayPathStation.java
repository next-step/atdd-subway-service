package nextstep.subway.path.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/14
 * @description :
 **/
public class SubwayPathStation {
	private Long id;
	private String name;
	private LocalDateTime createdAt;

	public SubwayPathStation(Long id, String name, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public SubwayPathStation(Station station) {
		this.id = station.getId();
		this.name = station.getName();
		this.createdAt = station.getCreatedDate();
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
		SubwayPathStation that = (SubwayPathStation)o;
		return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName())
			&& Objects.equals(getCreatedAt(), that.getCreatedAt());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName(), getCreatedAt());
	}
}
