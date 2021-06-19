package nextstep.subway.line.ui;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Line persistLine = lineService.saveLine(lineRequest);
        List<StationResponse> stations = persistLine.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.created(URI.create("/lines/" + persistLine.getId()))
                .body(LineResponse.of(persistLine, stations));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<Line> lines = lineService.findLines();
        return ResponseEntity.ok(lines.stream()
                .map(line -> {
                    List<StationResponse> stations = line.getStations().stream()
                            .map(StationResponse::of)
                            .collect(Collectors.toList());
                    return LineResponse.of(line, stations);
                })
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> getLineById(@PathVariable Long id) {
        Line line = lineService.findLineById(id);

        List<StationResponse> stations = line.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(LineResponse.of(line, stations));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(@PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest.toLine());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity addLineStation(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity removeLineStation(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }
}
