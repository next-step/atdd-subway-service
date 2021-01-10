package nextstep.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;

@Service
@Transactional(readOnly = true)
public class PathService {
	public PathResponse findPath(PathRequest request) {
		return null;
	}
}
