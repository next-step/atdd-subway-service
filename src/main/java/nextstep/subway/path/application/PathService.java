package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.path.dto.PathResponse;

@Service
public class PathService {

	public PathResponse find(int source, int target) {
		return new PathResponse(null, 0);
	}
}
