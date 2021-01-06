package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Paths;
import nextstep.subway.path.dto.PathResponseDto;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

	private final LineService lineService;
	private final StationService stationService;

	public PathService(LineService lineService,
		  StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	public PathResponseDto findPaths(Long source, Long target) {
		List<Section> sections = lineService.findAllSections();
		Station sourceStation = stationService.findById(source);
		Station targetStation = stationService.findById(target);

		Paths paths = new Paths(sections);
		return paths.getPath(sourceStation, targetStation);
	}
}
