package nextstep.subway.path.dto;

import static java.time.LocalDateTime.*;
import static java.util.Arrays.*;

import java.time.LocalDateTime;
import java.util.List;

public class PathResponse {

	public static class StationResponse {
		private Long id;
		private String name;
		private LocalDateTime createdAt;

		public StationResponse(Long id, String name, LocalDateTime createdAt) {
			this.id = id;
			this.name = name;
			this.createdAt = createdAt;
		}

		public StationResponse() {}

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

	private List<StationResponse> stations;
	private int distance;

	public PathResponse(List<StationResponse> stations, int distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public PathResponse() {}

	public static PathResponse createMock() {
		List<StationResponse> stationResponses = asList(
			new StationResponse(3L, "교대역", now()),
			new StationResponse(2L, "양재역", now()));

		return new PathResponse(stationResponses, 2);
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
