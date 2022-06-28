package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {
    private final static long INIT_PATH_STATION_ID = 0L;

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPaths(PathRequest pathRequest) {
        validPathRequest(pathRequest);
        return ResponseEntity.ok(
                pathService.findShortPath(pathRequest)
        );
    }

    private void validPathRequest(PathRequest pathRequest) {
        if (pathRequest == null) {
            throw new IllegalArgumentException("요청한 경로가 존재하지 않습니다.");
        }
        if (isInitPathSource(pathRequest)) {
            throw new IllegalArgumentException("source 값은 유효하지 않습니다");
        }
        if (isInitPathTarget(pathRequest)) {
            throw new IllegalArgumentException("target 값은 유효하지 않습니다");
        }
    }

    private boolean isInitPathTarget(PathRequest pathRequest) {
        return pathRequest.getTarget() == INIT_PATH_STATION_ID;
    }

    private boolean isInitPathSource(PathRequest pathRequest) {
        return pathRequest.getSource() == INIT_PATH_STATION_ID;
    }

}
