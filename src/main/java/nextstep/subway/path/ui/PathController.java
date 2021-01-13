package nextstep.subway.path.ui;

import nextstep.subway.path.application.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathController {

    private final PathFinder pathFinder;

    public PathController(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    /**
     * 출발역-도착역으로 가는 최단 거리 경로를 구합니다.
     * @param sourceStationId
     * @param targetStationId
     * @return
     */
    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> getPaths(@RequestParam(value = "source") Long sourceStationId
                                                ,@RequestParam(value = "target") Long targetStationId) {
        PathResponse pathResponse = this.pathFinder.getShortestPath(sourceStationId, targetStationId);
        return ResponseEntity.ok().body(pathResponse);
    }
}
