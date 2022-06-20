package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.section.application.SectionService;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final SectionService sectionService;

    public PathService(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    public PathResponse findShortestPath(final Long upStationId, final Long downStationId) {
        return null;
    }
}
