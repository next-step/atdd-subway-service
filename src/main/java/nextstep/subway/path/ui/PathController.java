package nextstep.subway.path.ui;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@RestController
@RequestMapping("/path")
public class PathController {
	private PathService pathService;

	public PathController(PathService pathservice) {
		this.pathService = pathservice;
	}

	@GetMapping
	public ResponseEntity findPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
		PathResponse pathResponse = pathService.findPath(sourceId, targetId);

		return ResponseEntity.ok().body(pathResponse);
	}
}
