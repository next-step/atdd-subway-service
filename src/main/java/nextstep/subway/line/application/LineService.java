package nextstep.subway.line.application;

import nextstep.subway.exception.AddLineSectionFailException;
import nextstep.subway.exception.RemoveSectionFailException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
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
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationService stationService, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        Line persistLine = lineRepository.save(Line.of(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        return createLineResponse(persistLine);
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = findAll();
        return persistLines.stream()
                .map(line -> createLineResponse(line))
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return createLineResponse(persistLine);
    }

    private List<StationResponse> createStations(Line persistLine) {
        return getStations(persistLine).stream()
                .map(it -> StationResponse.of(it))
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line persistLine) {
        return LineResponse.of(persistLine, createStations(persistLine));
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
        validationStationEquals(upStation, downStation);

        boolean isUpStationExisted = !sectionRepository.findByLineIdAndUpStationIdOrDownStationId(line.getId(), upStation.getId()).isEmpty();
        boolean isDownStationExisted = !sectionRepository.findByLineIdAndUpStationIdOrDownStationId(line.getId(), downStation.getId()).isEmpty();

        validationAddLineStation(line, isUpStationExisted, isDownStationExisted);
        Section section = new Section(line, upStation, downStation, request.getDistance());

        addSection(line, isUpStationExisted, isDownStationExisted, section);
    }

    private void validationStationEquals(Station upStation, Station downStation) {
        if(upStation.getId() == downStation.getId()) {
            throw new AddLineSectionFailException("두개의 역이 같습니다.");
        }
    }

    private void addSection(Line line, boolean isUpStationExisted, boolean isDownStationExisted, Section section) {
        if (isUpStationExisted) {
            line.addUpStationExisted(section);
            return ;
        }
        if (isDownStationExisted) {
            line.addDownStationExisted(section);
            return ;
        }
        throw new AddLineSectionFailException("역 정보를 찾지 못했습니다.");
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);

        if (line.sizeSections() <= 1) {
            throw new RemoveSectionFailException("구간의 길이가 1개 이하이므로 삭제할 수 없습니다.");
        }

        Optional<Section> upLineStation = sectionRepository.findByLine_IdAndUpStation_Id(line.getId(), station.getId());
        Optional<Section> downLineStation = sectionRepository.findByLine_IdAndDownStation_Id(line.getId(), station.getId());

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            addSection(line, upLineStation, downLineStation);
        }

        upLineStation.ifPresent(it -> line.removeLineStation(it));
        downLineStation.ifPresent(it -> line.removeLineStation(it));
    }

    private void addSection(Line line, Optional<Section> upLineStation, Optional<Section> downLineStation) {
        Station newUpStation = downLineStation.get().getUpStation();
        Station newDownStation = upLineStation.get().getDownStation();
        int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
        line.addSection(new Section(line, newUpStation, newDownStation, newDistance));
    }

    private void validationAddLineStation(Line line, boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new AddLineSectionFailException("이미 등록된 구간 입니다.");
        }

        if (isUpStationExisted && isDownStationExisted) {
            throw new AddLineSectionFailException("등록할 수 없는 구간 입니다.");
        }
    }

    public static List<Station> getStations(Line line) {
        if (line.emptySections()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation(line);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.getSections().getSections().stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private static Station findUpStation(Line line) {
        Station upStation = line.getSections().getSections().get(0).getUpStation();
        while (upStation != null) {
            Station finalDownStation = upStation;
            Optional<Section> nextLineStation = line.getSections().getSections().stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            upStation = nextLineStation.get().getUpStation();
        }

        return upStation;
    }

}
