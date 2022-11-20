package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTestFixture.createLine;
import static nextstep.subway.line.domain.SectionTestFixture.createSection;
import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 도메인 테스트")
public class SectionTest {

    private Station 신논현역;
    private Station 강남역;
    private Station 양재역;
    private Station 양재시민의숲역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        신논현역 = createStation("신논현역");
        강남역 = createStation("강남역");
        양재역 = createStation("양재역");
        양재시민의숲역 = createStation("양재시민의숲역");
        신분당선 = createLine("신분당선", "bg-red", 강남역, 양재역, 10);
    }

    @DisplayName("구간에 존재하는 지하철역들을 조회한다.")
    @Test
    void findStationInSection() {
        // given
        Section section = createSection(신분당선, 양재역, 양재시민의숲역, 15);

        // when
        List<Station> stations = section.stations();

        // then
        assertThat(stations).contains(양재역, 양재시민의숲역);
    }

    @DisplayName("구간 생성 시, 노선 정보가 없으면 예외가 발생한다.")
    @Test
    void createSectionThrowErrorWhenLineIsNull() {
        // when & then
        assertThatThrownBy(() -> createSection(null, 강남역, 양재역, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선_정보가_없음.getErrorMessage());
    }

    @DisplayName("구간 생성 시, 상행역과 하행역이 동일하면 예외가 발생한다.")
    @Test
    void createSectionThrowErrorWhenEqualStations() {
        // when & then
        assertThatThrownBy(() -> createSection(신분당선, 강남역, 강남역, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.구간의_상행역과_하행역이_동일할_수_없음.getErrorMessage());
    }

    @DisplayName("구간 생성 시, 상행역이 비어있으면 예외가 발생한다.")
    @Test
    void createSectionThrowErrorWhenUpStationsIsNull() {
        // when & then
        assertThatThrownBy(() -> createSection(신분당선, null, createStation("강남역"), 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.상행종착역은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("구간 생성 시, 하행역이 비어있으면 예외가 발생한다.")
    @Test
    void createSectionThrowErrorWhenDownStationsIsNull() {
        // when & then
        assertThatThrownBy(() -> createSection(신분당선, 양재역, null, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.하행종착역은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("구간들의 노선이 동일한지 비교한다.")
    @Test
    void checkSectionLineIsEqual() {
        // given
        Station 역삼역 = createStation("역삼역");
        Line otherLine = createLine("2호선", "bg-green", 강남역, 역삼역, 5);
        Section section = createSection(신분당선, 강남역, 양재역, 15);
        Section otherSection = createSection(otherLine, 강남역, 역삼역, 10);

        // when & then
        assertThat(section.hasSameLine(otherSection)).isFalse();
    }

    @DisplayName("구간의 상행역이 주어진 역과 동일한지 비교한다.")
    @Test
    void checkSectionUpStationIsEqual() {
        // given
        Section section = createSection(신분당선, 강남역, 양재시민의숲역, 15);

        // when & then
        assertThat(section.isSameUpStation(강남역)).isTrue();
    }

    @DisplayName("구간의 하행역이 주어진 역과 동일한지 비교한다.")
    @Test
    void checkSectionDownStationIsEqual() {
        // given
        Section section = createSection(신분당선, 신논현역, 강남역, 15);

        // when & then
        assertThat(section.isSameDownStation(강남역)).isTrue();
    }

    @DisplayName("구간의 상행역을 업데이트하면 구간 길이와 상행역이 바뀐다.")
    @Test
    void updateUpStationInSection() {
        // given
        int originalDistance = 25;
        Section section = createSection(신분당선, 신논현역, 양재역, originalDistance);

        // when
        int targetDistance = 10;
        section.updateUpStation(createSection(신분당선, 신논현역, 강남역, targetDistance));

        // then
        assertAll(
                () -> assertThat(section.getDistance()).isEqualTo(Distance.from(originalDistance - targetDistance)),
                () -> assertThat(section.getUpStation()).isEqualTo(강남역),
                () -> assertThat(section.getDownStation()).isEqualTo(양재역)
        );
    }

    @DisplayName("구간의 하행역을 업데이트하면 구간 길이와 하행역이 바뀐다.")
    @Test
    void updateDownStationInSection() {
        // given
        int originalDistance = 25;
        Section section = createSection(신분당선, 신논현역, 양재역, originalDistance);

        // when
        int targetDistance = 10;
        section.updateDownStation(createSection(신분당선, 강남역, 양재역, targetDistance));

        // then
        assertAll(
                () -> assertThat(section.getDistance()).isEqualTo(Distance.from(originalDistance - targetDistance)),
                () -> assertThat(section.getUpStation()).isEqualTo(신논현역),
                () -> assertThat(section.getDownStation()).isEqualTo(강남역)
        );
    }

    @DisplayName("구간의 길이를 더한다")
    @Test
    void addSectionDistance() {
        // given
        int originalDistance = 10;
        int targetDistance = 10;
        Section section = createSection(신분당선, 신논현역, 강남역, originalDistance);
        Section addSection = createSection(신분당선, 강남역, 양재역, targetDistance);

        // when
        Distance newDistance = section.findAddDistance(addSection);

        // then
        assertThat(newDistance).isEqualTo(Distance.from(originalDistance + targetDistance));
    }

    @DisplayName("구간의 노선/상행역/하행역이 모두 같으면 동일한 구간이다.")
    @Test
    void equalSection() {
        // given
        Section section = createSection(신분당선, 신논현역, 강남역, 10);
        Section duplicateSection = createSection(신분당선, 신논현역, 강남역, 10);

        // when & then
        assertThat(section.isSameSection(duplicateSection)).isTrue();
    }
}
