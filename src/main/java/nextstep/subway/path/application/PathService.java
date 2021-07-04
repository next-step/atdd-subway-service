package nextstep.subway.path.application;

import java.util.List;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationGraph;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.excpetion.StationGraphException;
import nextstep.subway.utils.FareCalculator;

@Service
@Transactional
public class PathService {

	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse findShortestDistance(Long source, Long target) {
		List<Line> lines = lineRepository.findAll();
		Station sourceStation = findStationById(source);
		Station targetStation = findStationById(target);
		StationGraph stationGraph = new StationGraph(new Lines(lines));
		GraphPath<Station, SectionEdge> path = stationGraph.getShortestPath(sourceStation, targetStation);
		Fare fare = FareCalculator.getSubwayFare(path, 10);//todo 나이필요
		return PathResponse.of(stationGraph.getShortestPath(sourceStation, targetStation), fare);
	}

	public Station findStationById(Long id) {
		return stationRepository.findById(id).orElseThrow(() -> new StationGraphException("존재하지 않는 역을 조회합니다."));
	}
}
