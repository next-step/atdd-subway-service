package nextstep.subway.path.domain;

import org.jgrapht.GraphPath;

/**
 * @author : byungkyu
 * @date : 2021/01/14
 * @description :
 **/
public interface PathFinderRepository {
	SubwayPath getDijkstraShortestPath(SubwayPathSections sections, SubwayPathStation sourcePathStation, SubwayPathStation targetPathStation);
}
