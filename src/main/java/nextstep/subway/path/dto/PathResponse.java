package nextstep.subway.path.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.dto.StationResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PathResponse {

	private List<StationResponse> stations;

	private double distance;

	private int fee;

	private PathResponse(List<StationResponse> stations, double distance) {
		this.stations = stations;
		this.distance = distance;
	}

	public static PathResponse of(List<StationResponse> stations, double distance) {
		return new PathResponse(stations, distance);
	}
}
