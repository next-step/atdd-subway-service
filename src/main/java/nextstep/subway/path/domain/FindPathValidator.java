package nextstep.subway.path.domain;

import org.springframework.stereotype.Service;

@Service
public class FindPathValidator {
	public void validate(long sourceStationId, long targetStationId) {
		if (sourceStationId == targetStationId) {
			throw new RuntimeException("출발역과 도착역은 같을 수 없습니다.");
		}
	}
}
