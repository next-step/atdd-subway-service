package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.domain.SectionsTestFixture.강남역;
import static nextstep.subway.line.domain.SectionsTestFixture.강남역_역삼역_구간;
import static nextstep.subway.line.domain.SectionsTestFixture.교대역;
import static nextstep.subway.line.domain.SectionsTestFixture.역삼역;
import static nextstep.subway.line.domain.SectionsTestFixture.역삼역_교대역_구간;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 모음 테스트")
@DataJpaTest
class SectionsTest {
    @DisplayName("생성 확인")
    @Test
    void 생성_확인() {
        // when
        Sections sections = Sections.of(Arrays.asList(강남역_역삼역_구간, 역삼역_교대역_구간));

        // then
        assertThat(sections)
                .isNotNull();
    }

    @DisplayName("역 목록 조회 확인")
    @Test
    void 역_목록_조회_확인() {
        // given
        Sections sections = Sections.of(Arrays.asList(강남역_역삼역_구간, 역삼역_교대역_구간));

        // when
        List<Station> stations = sections.getStations();

        // then
        지하철_노선에_지하철역_순서_정렬됨(stations, Arrays.asList(강남역, 역삼역, 교대역));
    }

    public static void 지하철_노선에_지하철역_순서_정렬됨(List<Station> stations, List<Station> expectedStations) {
        List<Long> stationIds = stations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        List<Long> expectedStationIds = expectedStations.stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }
}