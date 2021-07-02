package nextstep.subway.path.domain;

import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindPathValidator {
	public void validate(long sourceStationId, long targetStationId, List<StationResponse> stations) {
		if (sourceStationId == targetStationId) {
			throw new RuntimeException("출발역과 도착역은 같을 수 없습니다.");
		}

		List<Long> stationIds = stations.stream().map(StationResponse::getId)
				.collect(Collectors.toList());
		if (!stationIds.containsAll(Arrays.asList(sourceStationId, targetStationId))) {
			throw new RuntimeException("존재하지 않는 출발역이나 도착역은 선택할 수 없습니다");
		}
	}
}
