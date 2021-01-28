package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lines {

    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public int getDistance(List<Long> stationIds) {
        return IntStream.range(0, stationIds.size())
                .reduce((accum, idx) -> {
                    Long upStationId = stationIds.get(idx - 1);
                    Long downStationId = stationIds.get(idx);
                    Section section = findMustExistSectionById(upStationId, downStationId);
                    return accum + section.getDistance();
                })
                .getAsInt();
    }

    public List<Station> getStations(List<Long> stationIds) {
        return stationIds
                .stream()
                .map(this::findMustExistStationById)
                .collect(Collectors.toList());
    }

    private Station findMustExistStationById(Long id) {
        Optional<Line> containLine = lines.stream()
                .filter(line -> line.findStationOrNull(id) != null)
                .findFirst();
        if (containLine.isPresent()) {
            return containLine.get().findStationOrNull(id);
        }
        throw new RuntimeException("역이 존재하지 않습니다");
    }

    private Section findMustExistSectionById(Long upStationId, Long downStationId) {
        Optional<Line> containLine = lines.stream().filter(line -> line.findSectionOrNull(upStationId, downStationId) != null)
                .findFirst();
        if (containLine.isPresent()) {
            return containLine.get().findSectionOrNull(upStationId, downStationId);
        }
        throw new RuntimeException("구간이이 존재하지 않습니다");
    }
}
