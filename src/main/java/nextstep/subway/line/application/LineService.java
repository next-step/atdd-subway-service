package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

//    public LineResponse saveLine(LineRequest request) {
//        Station upStation = stationService.findById(request.getUpStationId());
//        Station downStation = stationService.findById(request.getDownStationId());
//        Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
//        List<StationResponse> stations = getStations(persistLine).stream()
//                .map(StationResponse::of)
//                .collect(Collectors.toList());
//        return LineResponse.of(persistLine, stations);
//    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());

        Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
        return LineResponse.of(persistLine);
    }

//    public List<LineResponse> findLines() {
//        List<Line> persistLines = lineRepository.findAll();
//        return persistLines.stream()
//                .map(line -> {
//                    List<StationResponse> stations = getStations(line).stream()
//                            .map(StationResponse::of)
//                            .collect(Collectors.toList());
//                    return LineResponse.of(line, stations);
//                })
//                .collect(Collectors.toList());
//    }

    public List<LineResponse> findLines() {
        return LineResponse.ofList(lineRepository.findAll());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }


    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
//        List<StationResponse> stations = getStations(persistLine).stream()
//                .map(it -> StationResponse.of(it))
//                .collect(Collectors.toList());

        return LineResponse.of(persistLine);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        line.addSection(new Section(line, upStation, downStation, request.getDistance()));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);

        line.removeSection(station);
    }


//    public List<Station> getStations(Line line) {
//        if (line.getSections().getSections().isEmpty()) {
//            return Arrays.asList();
//        }
//
//        List<Station> stations = new ArrayList<>();
//        Station downStation = findUpStation(line);
//        stations.add(downStation);
//
//        while (downStation != null) {
//            Station finalDownStation = downStation;
//            Optional<Section> nextLineStation = line.getSections().getSections().stream()
//                    .filter(it -> it.getUpStation() == finalDownStation)
//                    .findFirst();
//            if (!nextLineStation.isPresent()) {
//                break;
//            }
//            downStation = nextLineStation.get().getDownStation();
//            stations.add(downStation);
//        }
//
//        return stations;
//    }

//    private Station findUpStation(Line line) {
//        Station downStation = line.getSections().getSections().get(0).getUpStation();
//        while (downStation != null) {
//            Station finalDownStation = downStation;
//            Optional<Section> nextLineStation = line.getSections().getSections().stream()
//                    .filter(it -> it.getDownStation() == finalDownStation)
//                    .findFirst();
//            if (!nextLineStation.isPresent()) {
//                break;
//            }
//            downStation = nextLineStation.get().getUpStation();
//        }
//
//        return downStation;
//    }
}
