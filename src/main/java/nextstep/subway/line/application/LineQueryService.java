package nextstep.subway.line.application;

import nextstep.subway.line.application.exception.LineNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineQueryService {
    public static final String LINE_ID_NOT_FOUND_EXCEPTION_MESSAGE = "요청한 ID에 해당하는 노선을 찾을 수 없습니다.";

    private final LineRepository lineRepository;

    public LineQueryService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<LineResponse> findLines() {
        List<Line> persistLines = lineRepository.findAll();
        return LineResponse.ofList(persistLines);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(LINE_ID_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }
}
