package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTest.섹션_없는_라인_생성;
import static nextstep.subway.line.domain.SectionTest.빈_섹션_생성;
import static nextstep.subway.line.domain.SectionTest.섹션_생성;
import static nextstep.subway.line.domain.StationTest.역_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Sections 클래스")
public class SectionsTest {

    private Line 일호선;
    private Station 노량진역;
    private Station 용산역;
    private Station 남영역;
    private Section 노량진_용산_섹션;
    private Section 용산_남영_섹션;

    @BeforeEach
    void setUp() {
        일호선 = 섹션_없는_라인_생성("1호선", "bg-blue-600");
        노량진역 = 역_생성("노량진역");
        용산역 = 역_생성("용산역");
        남영역 = 역_생성("남영역");
        노량진_용산_섹션 = 섹션_생성(일호선, 노량진역, 용산역, 5);
        용산_남영_섹션 = 섹션_생성(일호선, 용산역, 남영역, 5);
    }

    @DisplayName("getStations 메서드")
    @Nested
    class GetStations {

        @DisplayName("섹션이 존재하지 않을 경우 빈 리스트를 반환한다.")
        @Test
        void sectionsEmpty() {
            //given
            final Sections 빈_섹션 = 빈_섹션_생성();

            //when
            final List<Station> 빈_역_목록 = 빈_섹션.getStations();

            //then
            assertThat(빈_역_목록.isEmpty()).isTrue();
        }

        @DisplayName("섹션이 존재할 경우 정렬된 역 리스트를 반환한다.")
        @Test
        void sectionsExist() {
            //given
            final Sections 섹션_리스트 = 빈_섹션_생성();
            섹션_리스트.addSection(노량진_용산_섹션);
            섹션_리스트.addSection(용산_남영_섹션);

            //when
            final List<Station> 정렬된_역_목록 = 섹션_리스트.getStations();

            //then
            첫번째역_확인(정렬된_역_목록, 노량진역);
            두번째역_확인(정렬된_역_목록, 용산역);
            세번째역_확인(정렬된_역_목록, 남영역);
        }

    }

    private void 첫번째역_확인(final List<Station> stations, final Station station) {
        assertThat(stations.get(0)).isEqualTo(station);
    }

    private void 두번째역_확인(final List<Station> stations, final Station station) {
        assertThat(stations.get(1)).isEqualTo(station);
    }

    private void 세번째역_확인(final List<Station> stations, final Station station) {
        assertThat(stations.get(2)).isEqualTo(station);
    }
}
