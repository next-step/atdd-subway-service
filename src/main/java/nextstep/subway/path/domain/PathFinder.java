package nextstep.subway.path.domain;

import org.springframework.stereotype.Service;

/**
 * @author : byungkyu
 * @date : 2021/01/14
 * @description :
 **/

@Service
public class PathFinder {
	private PathFinderRepository pathFinderRepository;

	public PathFinder(PathFinderRepository pathFinderRepository) {
		this.pathFinderRepository = pathFinderRepository;
	}

	public SubwayPath findShortestPath(SubwayPathSections sections, SubwayPathStation sourcePathStation,
		SubwayPathStation targetPathStation) {
		return pathFinderRepository.getDijkstraShortestPath(sections, sourcePathStation,
			targetPathStation);
	}
}
