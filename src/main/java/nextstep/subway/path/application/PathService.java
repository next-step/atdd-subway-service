package nextstep.subway.path.application;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import nextstep.subway.path.dto.PathResponse;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
@Service
@Transactional
public class PathService {

	public PathResponse findShortestPath(Long sourceId, Long targetId) {
		return new PathResponse();
	}
}
