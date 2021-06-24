package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/paths")
@RestController
public class PathController {

  private final PathService pathService;

  public PathController(PathService pathService) {
    this.pathService = pathService;
  }

  @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, headers = {"Authorization"})
  public ResponseEntity<PathResponse> findShortestPath(@AuthenticationPrincipal LoginMember loginMember,
                                                       @RequestParam("source") Long sourceStationId,
                                                       @RequestParam("target") Long targetStationId) {
    PathResponse shortestPath = pathService.findShortestPathForLoginMember(loginMember, sourceStationId, targetStationId);
    return ResponseEntity.ok(shortestPath);
  }

  @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<PathResponse> findShortestPath(@RequestParam("source") Long sourceStationId,
                                                       @RequestParam("target") Long targetStationId) {
    PathResponse shortestPath = pathService.findShortestPath(sourceStationId, targetStationId);
    return ResponseEntity.ok(shortestPath);
  }
}
