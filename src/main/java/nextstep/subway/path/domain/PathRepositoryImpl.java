package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

/**
 * @author : byungkyu
 * @date : 2021/01/13
 * @description :
 **/
@Repository
public class PathRepositoryImpl implements PathRepository{
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathRepositoryImpl(LineRepository lineRepository,
		StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Override
	public SubwayPathSections findSubwayPathSectionAll() {
		List<Line> allLines = lineRepository.findAll();
		List<SubwayPathSection> subwayPathSections = allLines.stream()
			.map(line -> line.getSections())
			.flatMap(Collection::stream)
			.map(section -> sectionToSubwayPathSection(section))
			.collect(Collectors.toList());
		return new SubwayPathSections(subwayPathSections);
	}

	private SubwayPathSection sectionToSubwayPathSection(Section section) {
		return new SubwayPathSection(section.getUpStation(), section.getDownStation(), section.getDistance());
	}

	@Override
	public SubwayPathStation findStationById(Long sourceId) {
		Station station = stationRepository.findById(sourceId).orElseThrow(() -> new RuntimeException());
		return new SubwayPathStation(station);
	}

}
