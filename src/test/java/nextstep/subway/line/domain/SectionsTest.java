package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.DistanceTest.거리_생성;
import static nextstep.subway.line.domain.LineTest.섹션_없는_라인_생성;
import static nextstep.subway.line.domain.SectionTest.빈_섹션_생성;
import static nextstep.subway.line.domain.StationTest.역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    private Station 서울역;
    private Distance 거리_5;
    private Distance 거리_3;

    @BeforeEach
    void setUp() {
        일호선 = 섹션_없는_라인_생성("1호선", "bg-blue-600");
        노량진역 = 역_생성("노량진역");
        용산역 = 역_생성("용산역");
        남영역 = 역_생성("남영역");
        서울역 = 역_생성("서울역");
        거리_5 = 거리_생성(5);
        거리_3 = 거리_생성(3);
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
            섹션_리스트.addSection(일호선, 노량진역, 용산역, 거리_5);
            섹션_리스트.addSection(일호선, 용산역, 남영역, 거리_5);

            //when
            final List<Station> 정렬된_역_목록 = 섹션_리스트.getStations();

            //then
            첫번째역_확인(정렬된_역_목록, 노량진역);
            두번째역_확인(정렬된_역_목록, 용산역);
            세번째역_확인(정렬된_역_목록, 남영역);
        }

    }

    @DisplayName("addSection 메서드")
    @Nested
    class AddSection {
        @DisplayName("상행 하행 모두 이미 등록된 구간일 경우 익셉션 발생한다.")
        @Test
        void alreadyRegisteredSection() {
            //given
            final Sections 섹션_리스트 = 빈_섹션_생성();
            섹션_리스트.addSection(일호선, 노량진역, 용산역, 거리_5);

            //when
            //then
            assertThatThrownBy(() -> 섹션_리스트.addSection(일호선, 노량진역, 용산역, 거리_5))
                    .isInstanceOf(RuntimeException.class);
        }

        @DisplayName("상행 하행 모두 등록되지 않은 구간일 경우 익셉션 발생한다.")
        @Test
        void bothNotRegisteredSection() {
            //given
            final Sections 섹션_리스트 = 빈_섹션_생성();
            섹션_리스트.addSection(일호선, 노량진역, 용산역, 거리_5);

            //when
            //then
            assertThatThrownBy(() -> 섹션_리스트.addSection(일호선, 남영역, 서울역, 거리_5))
                    .isInstanceOf(RuntimeException.class);
        }

        @DisplayName("역이 하나도 등록되지 않았으면 등록에 성공한다.")
        @Test
        void stationIsEmpty() {
            //given
            final Sections 섹션_리스트 = 빈_섹션_생성();

            //when
            섹션_리스트.addSection(일호선, 노량진역, 용산역, 거리_5);

            //then
            final List<Station> 정렬된_역_목록 = 섹션_리스트.getStations();
            첫번째역_확인(정렬된_역_목록, 노량진역);
            두번째역_확인(정렬된_역_목록, 용산역);
        }

        @DisplayName("상행역이 일치하면 상행역 앞에 등록한다.")
        @Test
        void equalsUpStation() {
            //given
            final Sections 섹션_리스트 = 빈_섹션_생성();
            섹션_리스트.addSection(일호선, 용산역, 서울역, 거리_5);

            //when
            섹션_리스트.addSection(일호선, 용산역, 남영역, 거리_3);

            //then
            final List<Station> 정렬된_역_목록 = 섹션_리스트.getStations();
            첫번째역_확인(정렬된_역_목록, 용산역);
            두번째역_확인(정렬된_역_목록, 남영역);
            세번째역_확인(정렬된_역_목록, 서울역);
        }

        @DisplayName("하행역이 일치하면 하행역 뒤에 등록한다.")
        @Test
        void equalsDownStation() {
            //given
            final Sections 섹션_리스트 = 빈_섹션_생성();
            섹션_리스트.addSection(일호선, 노량진역, 남영역, 거리_5);

            //when
            섹션_리스트.addSection(일호선, 용산역, 남영역, 거리_3);

            //then
            final List<Station> 정렬된_역_목록 = 섹션_리스트.getStations();
            첫번째역_확인(정렬된_역_목록, 노량진역);
            두번째역_확인(정렬된_역_목록, 용산역);
            세번째역_확인(정렬된_역_목록, 남영역);
        }
    }

    @DisplayName("removeSection 메서드")
    @Nested
    class RemoveSection {
        @DisplayName("섹션이 하나 이하로 존재하면 익셉션 발생한다.")
        @Test
        void alreadyRegisteredSection() {
            //given
            final Sections 섹션_리스트 = 빈_섹션_생성();
            섹션_리스트.addSection(일호선, 노량진역, 용산역, 거리_5);

            //when
            //then
            assertThatThrownBy(() -> 섹션_리스트.removeSection(일호선, 노량진역))
                    .isInstanceOf(RuntimeException.class);
        }

        @DisplayName("역이 일치하는 구간이 하나면 해당하는 구간을 삭제한다.")
        @Test
        void oneStationEquals() {
            //given
            final Sections 섹션_리스트 = 빈_섹션_생성();
            섹션_리스트.addSection(일호선, 노량진역, 용산역, 거리_5);
            섹션_리스트.addSection(일호선, 용산역, 남영역, 거리_5);

            //when
            섹션_리스트.removeSection(일호선, 남영역);

            //then
            final List<Station> 정렬된_역_목록 = 섹션_리스트.getStations();
            역_개수_확인(정렬된_역_목록, 2);
            첫번째역_확인(정렬된_역_목록, 노량진역);
            두번째역_확인(정렬된_역_목록, 용산역);
        }

        @DisplayName("역이 일치하는 구간이 두개면 역을 삭제하고 구간을 합친다.")
        @Test
        void twoStationEquals() {
            //given
            final Sections 섹션_리스트 = 빈_섹션_생성();
            섹션_리스트.addSection(일호선, 노량진역, 용산역, 거리_5);
            섹션_리스트.addSection(일호선, 용산역, 남영역, 거리_5);

            //when
            섹션_리스트.removeSection(일호선, 용산역);

            //then
            final List<Station> 정렬된_역_목록 = 섹션_리스트.getStations();
            역_개수_확인(정렬된_역_목록, 2);
            첫번째역_확인(정렬된_역_목록, 노량진역);
            두번째역_확인(정렬된_역_목록, 남영역);
        }
    }

    private void 역_개수_확인(final List<Station> stations, final int size) {
        assertThat(stations.size()).isEqualTo(size);
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
