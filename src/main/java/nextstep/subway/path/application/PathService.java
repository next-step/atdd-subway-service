package nextstep.subway.path.application;

import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathRepository;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.path.domain.SubwayPathSections;
import nextstep.subway.path.domain.SubwayPathStation;
import nextstep.subway.path.dto.PathResponse;

/**
 * @author : byungkyu
 * @date : 2021/01/13
 * @description :
 **/
@Service
@Transactional
public class PathService {
	private PathRepository pathRepository;
	private PathFinder pathFinder;

	public PathService(PathRepository pathRepository, PathFinder pathFinder) {
		this.pathRepository = pathRepository;
		this.pathFinder = pathFinder;
	}

	public PathResponse findShortestPath(Long sourceId, Long targetId) {
		SubwayPathSections subwayPathSections = pathRepository.findSubwayPathSectionAll();
		SubwayPathStation sourcePathStation = pathRepository.findStationById(sourceId);
		SubwayPathStation targetPathStation = pathRepository.findStationById(targetId);

		SubwayPath shortestPath = pathFinder.findShortestPath(subwayPathSections, sourcePathStation, targetPathStation);

		return new PathResponse(shortestPath);
	}
}
