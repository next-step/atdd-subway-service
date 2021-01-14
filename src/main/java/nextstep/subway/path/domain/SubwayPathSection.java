package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/14
 * @description :
 **/
public class SubwayPathSection {
	private SubwayPathStation upStation;
	private SubwayPathStation downStation;
	private int distance;

	public SubwayPathSection(Station upStation, Station downStation, int distance) {
		this.upStation = new SubwayPathStation(upStation);
		this.downStation = new SubwayPathStation(downStation);
		this.distance = distance;
	}

	public List<SubwayPathStation> getSubwayPathStations(){
		return Arrays.asList(upStation,downStation);
	}

	public SubwayPathStation getUpStation() {
		return upStation;
	}

	public SubwayPathStation getDownStation() {
		return downStation;
	}

	public int getDistance() {
		return distance;
	}
}
