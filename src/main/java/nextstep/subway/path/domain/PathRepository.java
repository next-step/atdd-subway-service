package nextstep.subway.path.domain;

/**
 * @author : byungkyu
 * @date : 2021/01/13
 * @description :
 **/
public interface PathRepository {
	SubwayPathSections findSubwayPathSectionAll();
	SubwayPathStation findStationById(Long sourceId);

}
