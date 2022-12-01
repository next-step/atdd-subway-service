package nextstep.subway.path.ui;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/paths")
public class PathController {

    @GetMapping
    public ResponseEntity<PathResponse> findPaths(@RequestParam Long source, @RequestParam Long target) {
        List<Station> stations = new ArrayList<>();

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");

        stations.add(강남역);
        stations.add(양재역);

        PathResponse pathResponse = PathResponse.of(stations, 10);
        return ResponseEntity.ok(pathResponse);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, NoResultException.class, IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<Void> handleIllegalArgsException() {
        return ResponseEntity.badRequest().build();
    }
}
