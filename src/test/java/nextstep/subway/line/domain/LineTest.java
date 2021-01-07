package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    private Line 팔호선;
    private Station 천호역;
    private Station 잠실역;
    private Station 문정역;
    private Station 산성역;

    @BeforeEach
    void setUp() {
        천호역 = new Station("천호역");
        잠실역 = new Station("잠실역");
        문정역 = new Station("문정역");
        산성역 = new Station("산성역");

        팔호선 = Line.builder()
                .name("팔호선")
                .color("pink")
                .upStation(천호역)
                .downStation(산성역)
                .distance(30)
                .build();

        Section 천호역_잠실역 = Section.builder().line(팔호선)
                .upStation(천호역)
                .downStation(잠실역)
                .distance(new Distance(10))
                .build();

        팔호선.add(천호역_잠실역);
    }

    @DisplayName("지하철 라인에 구간을 등록")
    @Test
    void addSectionInLine() {
        // given
        Section newSection = Section.builder().line(팔호선)
                .upStation(잠실역)
                .downStation(문정역)
                .distance(new Distance(5))
                .build();

        // when
        팔호선.add(newSection);

        // then
        assertThat(팔호선.getStations()).containsExactlyElementsOf(Arrays.asList(천호역, 잠실역, 문정역, 산성역));
        assertThat(라인전체길이()).isEqualTo(30);
    }

    @DisplayName("지하철 라인의 중간역 삭제")
    @Test
    void deleteStationInLine() {
        // when
        팔호선.remove(잠실역);

        // then
        assertThat(팔호선.getStations()).containsExactlyElementsOf(Arrays.asList(천호역, 산성역));
        assertThat(라인전체길이()).isEqualTo(30);
    }

    private int 라인전체길이() {
        return 팔호선.getSections().stream()
                .map(section -> section.getDistance())
                .mapToInt(Distance::get)
                .sum();
    }
}
