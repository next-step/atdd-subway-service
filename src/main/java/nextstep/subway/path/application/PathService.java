package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.dto.PathResponseDto;
import org.springframework.stereotype.Service;

@Service
public class PathService {

	private LineService lineService;

	public PathResponseDto findPaths(Long source, Long target) {
		return new PathResponseDto(null, 0);
	}
}
