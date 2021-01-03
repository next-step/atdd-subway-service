package nextstep.subway.path.service;

import nextstep.subway.path.dto.PathResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private PathService pathService;

    @DisplayName("")
    @Test
    void name() {
        when(pathService.findPath(1L, 1L)).thenReturn(new PathResponse(Collections.emptyList(), 0));
    }
}