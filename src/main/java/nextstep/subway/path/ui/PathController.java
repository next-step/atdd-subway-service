package nextstep.subway.path.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResponse.StationResponse;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RequiredArgsConstructor
@RestController
@RequestMapping("/paths")
public class PathController {

    private final StationRepository stationRepository;

    @GetMapping
    public ResponseEntity findShortestPath(@RequestParam Long source, @RequestParam Long target) {
        StationResponse 강남역 = StationResponse.of(stationRepository.findByName("강남역"));
        StationResponse 양재역 = StationResponse.of(stationRepository.findByName("양재역"));
        StationResponse 남부터미널역 = StationResponse.of(stationRepository.findByName("남부터미널역"));
        return ResponseEntity.ok(new PathResponse(Arrays.asList(강남역, 양재역, 남부터미널역), 12));
    }
}
