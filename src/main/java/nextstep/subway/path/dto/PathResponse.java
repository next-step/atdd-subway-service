package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.fare.Money;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@AllArgsConstructor
@Getter
public class PathResponse {
	private final List<StationResponse> stations;
	private final int distance;
	private final long fare;

	public static PathResponse of(List<Station> stations, Distance distance, Money fare) {
		List<StationResponse> responses = stations.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());

		return new PathResponse(responses, distance.getDistance(), fare.getMoney());
	}
}
