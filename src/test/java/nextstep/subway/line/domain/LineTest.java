package nextstep.subway.line.domain;

import nextstep.subway.line.application.exception.InvalidSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static nextstep.subway.line.domain.Distance.MIN_DISTANCE;
import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 기능")
class LineTest {

    private Line line;

    @BeforeEach
    void setUp() {
        line = new Line("신분당선", "bg-red-600");
    }

    @DisplayName("종점역을 연장한 구간 추가한다.")
    @Test
    void addTerminusExtend() {
        // given
        line.addSection(getSection(Station.of("양재", "판교"), 10));
        Section upStationExtend = getSection(Station.of("강남", "양재"), 3);
        Section downStationExtend = getSection(Station.of("판교", "광교"), 5);

        // when
        line.addSection(upStationExtend);
        line.addSection(downStationExtend);

        // then
        assertThat(line.getSections())
                .extracting("upStation.name", "downStation.name", "distance.distance")
                .containsExactly(
                        tuple("강남", "양재", 3),
                        tuple("양재", "판교", 10),
                        tuple("판교", "광교", 5)
                );
    }

    @DisplayName("구간 사이에 새로운 구간을 추가한다.")
    @Test
    void addBetweenStations() {
        // given
        line.addSection(getSection(Station.of("강남", "청계산입구"), 9));
        Section newUpSection = getSection(Station.of("강남", "양재"), 3);
        Section newDownSection = getSection(Station.of("양재시민의숲", "청계산입구"), 4);

        // when
        line.addSection(newUpSection);
        line.addSection(newDownSection);

        // then
        assertThat(line.getSections())
                .extracting("upStation.name", "downStation.name", "distance.distance")
                .containsExactly(
                        tuple("강남", "양재", 3),
                        tuple("양재", "양재시민의숲", 2),
                        tuple("양재시민의숲", "청계산입구", 4)
                );
    }

    @Test
    @DisplayName("연결 가능한 구간이 없는 경우 예외가 발생한다.")
    void validateNotConnectable() {
        // given
        Section section1 = getSection(Station.of("강남", "양재"), 5);
        Section section2 = getSection(Station.of("판교", "광교"), 5);
        line.addSection(section1);

        // when // then
        assertThatThrownBy(() -> line.addSection(section2))
                .isInstanceOf(InvalidSectionException.class);
    }

    @Test
    @DisplayName("구간이 중복되는 경우 예외가 발생한다.")
    void validateDuplication() {
        // given
        Section section1 = getSection(Station.of("강남", "양재"), 5);
        Section section2 = getSection(Station.of("강남", "양재"), 5);
        line.addSection(section1);

        // when // then
        assertThatThrownBy(() -> line.addSection(section2))
                .isInstanceOf(InvalidSectionException.class);
    }

    @Test
    @DisplayName("구간 거리가 " + MIN_DISTANCE + "보다 작은 경우 예외가 발생한다.")
    void validateShortMinDistance() {
        // given
        List<Station> stations = Station.of("강남", "양재");

        // when // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> getSection(stations, 0));
    }

    @DisplayName("구간 거리가 기존 역 사이의 거리보다 크거나 같은 경우 예외가 발생한다.")
    @ParameterizedTest(name = "[{index}]구간 사이 거리")
    @ValueSource(ints = {5, 6})
    void validateLongDistanceBetweenSection(int invalidDistance) {
        // given
        int distance = 5;
        Section section1 = getSection(Station.of("강남", "양재시민의숲"), distance);
        Section section2 = getSection(Station.of("양재", "양재시민의숲"), invalidDistance);
        line.addSection(section1);

        // when // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> line.addSection(section2));
    }

    @Test
    @DisplayName("노선에 속한 지하철 역이 상행역 부터 하행역 순으로 정렬된다.")
    void sortLineStations() {
        // given
        line.addSection(getSection(Station.of("양재시민의숲", "판교"), 10));
        line.addSection(getSection(Station.of("청계산입구", "판교"), 5));
        line.addSection(getSection(Station.of("강남", "양재시민의숲"), 10));
        line.addSection(getSection(Station.of("강남", "양재"), 5));

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations)
                .extracting("name")
                .containsExactly("강남", "양재", "양재시민의숲", "청계산입구", "판교");
    }

    @Test
    @DisplayName("노선에 속한 지하철 역이 없으면 빈 목록이 조회된다.")
    void emptyStations() {
        // given
        Line line = new Line("신분당선", "bg-red-600");

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).isEmpty();
    }

    @Test
    @DisplayName("노선의 종점 구간을 제거한다.")
    void removeTerminusSection() {
        // given
        line.addSection(getSection(Station.of("강남", "양재"), 3));
        line.addSection(getSection(Station.of("양재", "양재시민의숲"), 3));
        line.addSection(getSection(Station.of("양재시민의숲", "판교"), 3));

        // when
        line.removeSection(new Station("강남"));
        line.removeSection(new Station("판교"));

        // then
        assertThat(line.getSections())
                .extracting("upStation.name", "downStation.name", "distance.distance")
                .containsExactly(
                        tuple("양재", "양재시민의숲", 3)
                );
    }

    @DisplayName("노선의 중간에 있는 구간을 제거한다.")
    @Test
    void removeBetweenStations() {
        // given
        line.addSection(getSection(Station.of("강남", "양재"), 3));
        line.addSection(getSection(Station.of("양재", "양재시민의숲"), 3));
        line.addSection(getSection(Station.of("양재시민의숲", "판교"), 3));

        // when
        line.removeSection(new Station("양재시민의숲"));

        // then
        assertThat(line.getSections())
                .extracting("upStation.name", "downStation.name", "distance.distance")
                .containsExactly(
                        tuple("강남", "양재", 3),
                        tuple("양재", "판교", 6)
                );
    }

    @DisplayName("구간이 1개인 경우 제거 시 예외가 발생한다.")
    @Test
    void removeValidateSize() {
        // given
        Section section = getSection(Station.of("양재", "양재시민의숲"), 5);
        line.addSection(section);

        // when // then
        assertThatThrownBy(() -> line.removeSection(new Station("양재시민의숲")))
                .isInstanceOf(InvalidSectionException.class);
    }

    private Section getSection(List<Station> stations, int distance) {
        return new Section(stations.get(0), stations.get(1), distance);
    }
}
