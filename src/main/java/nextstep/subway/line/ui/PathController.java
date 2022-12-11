package nextstep.subway.line.ui;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PathController {
    private final StationRepository stationRepository;

    @Autowired
    public PathController(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @GetMapping("/paths")
    public ResponseEntity<List<StationResponse>> findLineById(@RequestParam("source") String source, @RequestParam("target") String target) {
        Station sourceStation = stationRepository.findByName("교대역");
        Station middleStation = stationRepository.findByName("남부터미널역");
        Station targetStation = stationRepository.findByName("양재역");
        List<Station> stationList = Arrays.asList(sourceStation, middleStation, targetStation);

        List<StationResponse> returnList = stationList.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList())
                ;

        return ResponseEntity.ok(returnList);
    }
}
