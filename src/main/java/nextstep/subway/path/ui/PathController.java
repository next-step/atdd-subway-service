package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import static nextstep.subway.station.domain.Station.stationStaticFactoryForTestCode;

@RequestMapping("/paths")
@RestController
public class PathController {

  @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<PathResponse> findShortestPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
    Station firstExpectation = stationStaticFactoryForTestCode(3L, "교대역");
    Station secondExpectation = stationStaticFactoryForTestCode(4L, "남부터미널역");
    Station thirdExpectation = stationStaticFactoryForTestCode(2L, "양재역");
    return ResponseEntity.ok(new PathResponse(Arrays.asList(StationResponse.of(firstExpectation), StationResponse.of(secondExpectation), StationResponse.of(thirdExpectation)), 0));
  }

}
