package nextstep.subway.favorite.dto;

import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.util.Objects;

public class FavoriteStationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	protected FavoriteStationResponse() {
	}

	public FavoriteStationResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		this.id = id;
		this.name = name;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public static FavoriteStationResponse of(Station station) {
		return new FavoriteStationResponse(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FavoriteStationResponse that = (FavoriteStationResponse) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(name, that.name) &&
				Objects.equals(createdDate, that.createdDate) &&
				Objects.equals(modifiedDate, that.modifiedDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, createdDate, modifiedDate);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
