package nextstep.subway.path.service;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationService stationService;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationService);
    }

    @Test
    void findPath() {
        Long source = 1L;
        Long target = 4L;
    }
}
