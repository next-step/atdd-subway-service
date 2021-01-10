package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.OptionalLoginMember;
import nextstep.subway.auth.domain.OptionalLoginMemberPrincipal;
import nextstep.subway.path.application.PathCalculateException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paths")
public class PathController {

	private final PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("")
	public ResponseEntity<PathResponse> showPath(@OptionalLoginMemberPrincipal OptionalLoginMember optionalLoginMember,
	                                             @ModelAttribute PathRequest pathRequest) {
		return ResponseEntity.ok(pathService.calculatePath(pathRequest));
	}

	@ExceptionHandler(PathCalculateException.class)
	public ResponseEntity handlePathCalculateException(PathCalculateException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handleRuntimeException(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
