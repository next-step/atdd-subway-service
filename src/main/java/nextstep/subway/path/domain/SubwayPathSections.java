package nextstep.subway.path.domain;

import java.util.List;

/**
 * @author : byungkyu
 * @date : 2021/01/14
 * @description :
 **/
public class SubwayPathSections  {
	private List<SubwayPathSection> subwayPathSections;

	public SubwayPathSections() {
	}

	public SubwayPathSections(List<SubwayPathSection> subwayPathSections) {
		this.subwayPathSections = subwayPathSections;
	}

	public List<SubwayPathSection> getSubwayPathSections() {
		return subwayPathSections;
	}


}
