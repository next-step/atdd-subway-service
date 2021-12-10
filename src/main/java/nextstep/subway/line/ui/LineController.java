package nextstep.subway.line.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.common.util.ExtraChargeGenerator;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("lines")
@RequiredArgsConstructor
public class LineController {
    private final LineService lineService;

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        lineRequest.useExtraCharge(ExtraChargeGenerator.generate());
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        return ResponseEntity.ok(lineService.findLines());
    }

    @GetMapping("{id}")
    public ResponseEntity<LineResponse> findLineById(@PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineUpdateRequest lineUpdateRequest) {
        lineService.updateLine(id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{lineId}/sections")
    public ResponseEntity<Void> addLineStation(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addLineStation(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{lineId}/sections")
    public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.ok().build();
    }
}
