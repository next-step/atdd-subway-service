package nextstep.subway.path.ui;

import nextstep.subway.path.dto.LinePathRequest;
import nextstep.subway.path.dto.LinePathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/paths")
public class PathController {
    @GetMapping
    public ResponseEntity<LinePathResponse> paths(LinePathRequest linePathRequest) {
        return ResponseEntity.ok(
                new LinePathResponse(
                        Arrays.asList(
                                new StationResponse(1L, null, null, null),
                                new StationResponse(4L, null, null, null)
                        ),
                        3
                )
        );
    }
}
