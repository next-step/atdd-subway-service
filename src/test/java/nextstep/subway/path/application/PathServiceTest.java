package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.domain.adapters.SafeLineAdapter;
import nextstep.subway.path.domain.adapters.SafeStationAdapter;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPath;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPaths;
import nextstep.subway.path.domain.fee.transferFee.LineWithExtraFee;
import nextstep.subway.path.ui.dto.PathResponse;
import nextstep.subway.path.ui.dto.StationInPathResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    private PathService pathService;

    @Mock
    private SafeLineAdapter safeLineAdapter;

    @Mock
    private SafeStationAdapter safeStationAdapter;

    @BeforeEach
    void setup() {
        pathService = new PathService(safeLineAdapter, safeStationAdapter);
    }

    @DisplayName("최단 경로를 찾아서 응답할 수 있다.")
    @Test
    void findShortestPathTest() {
        Long sourceId = 1L;
        Long destinationId = 4L;
        LoginMember loginMember = new LoginMember(1L, "test@nextstep.com", 30);

        LineOfStationInPaths transferOnce = new LineOfStationInPaths(Arrays.asList(
                new LineOfStationInPath(Collections.singletonList(new LineWithExtraFee(1L, BigDecimal.ZERO))),
                new LineOfStationInPath(Arrays.asList(new LineWithExtraFee(1L, BigDecimal.ZERO), new LineWithExtraFee(2L, BigDecimal.TEN))),
                new LineOfStationInPath(Collections.singletonList(new LineWithExtraFee(2L, BigDecimal.TEN)))
        ));

        given(safeLineAdapter.getAllStationIds()).willReturn(Arrays.asList(1L, 2L, 3L, 4L));
        given(safeLineAdapter.getAllSafeSectionInfos()).willReturn(Arrays.asList(
                new SafeSectionInfo(1L, 2L, 5),
                new SafeSectionInfo(2L, 3L, 5),
                new SafeSectionInfo(2L, 4L, 5)
        ));
        given(safeStationAdapter.findStationsById(any())).willReturn(Arrays.asList(
                new SafeStationInfo(1L, "강남역", null),
                new SafeStationInfo(2L, "역삼역", null),
                new SafeStationInfo(4L, "삼성역", null)
        ));
        given(safeLineAdapter.getLineOfStationInPaths(any())).willReturn(transferOnce);

        PathResponse pathResponse = pathService.findShortestPath(sourceId, destinationId, loginMember);

        assertThat(pathResponse.getStations()).contains(
                new StationInPathResponse(1L, "강남역", null),
                new StationInPathResponse(2L, "역삼역", null),
                new StationInPathResponse(4L, "삼성역", null)
        );
        assertThat(pathResponse.getDistance()).isEqualTo(10);
    }
}