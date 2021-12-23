package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.line.domain.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리별 추가 운임")
class DistanceFareTest {
    
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "51:2150", "178:3650"}, delimiter = ':')
    @DisplayName("성인 운임 기준으로 거리별 추가 운임 계산")
    void distanceFare(int distance, int fare) {
        // given
        Route mockRoute = Mockito.mock(Route.class);
        Line mockLine = Mockito.mock(Line.class);
        Section mockSection = Mockito.mock(Section.class);
        Mockito.when(mockRoute.getDistance()).thenReturn(new Distance(distance));
        Mockito.when(mockRoute.getSections()).thenReturn(Arrays.asList(mockSection));
        Mockito.when(mockSection.getLine()).thenReturn(mockLine);

        // when
        DistanceFare distanceFare = new DistanceFare(mockRoute);

        // then
        assertThat(distanceFare.calculateAmount()).isEqualTo(new Amount(fare));
    }


}
