package nextstep.subway.line.domain;

import static nextstep.subway.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

    private Station 잠실역;
    private Station 선릉역;
    private Station 교대역;
    private Station 당산역;
    private Station 시청역;
    private final Sections sections = new Sections();
    Line line = new Line();

    @BeforeEach
    void setUp() {
        잠실역 = new Station("잠실역");
        선릉역 = new Station("선릉역");
        교대역 = new Station("교대역");
        당산역 = new Station("당산역");
        시청역 = new Station("시청역");

        sections.add(new Section(line, 교대역, 당산역, 100));
        sections.add(new Section(line, 잠실역, 선릉역, 10));
        sections.add(new Section(line, 당산역, 시청역, 30));
        sections.add(new Section(line, 선릉역, 교대역, 10));
    }

    @DisplayName("지하철 노선 역 목록을 가져온다.")
    @Test
    void getStations() {
        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(잠실역, 선릉역, 교대역, 당산역, 시청역);
    }

    @DisplayName("지하철 노선에 구간이 존재하지 않을경우 빈 역 목록을 가져온다.")
    @Test
    void getEmptyStations() {
        // given
        Sections sections = new Sections();

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).isEmpty();
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addSection() {
        // given
        Station 동대문역사문화공원 = new Station("동대문역사문화공원");
        Station 종합운동장 = new Station("종합운동장");
        Station 삼성역 = new Station("삼성역");
        Station 사당역 = new Station("사당역");

        // when
        sections.addSection(new Section(line, 동대문역사문화공원, 잠실역, 10));
        sections.addSection(new Section(line, 잠실역, 종합운동장, 2));
        sections.addSection(new Section(line, 종합운동장, 삼성역, 3));
        sections.addSection(new Section(line, 교대역, 사당역, 5));

        // then
        assertThat(sections.getStations()).containsExactly(
            동대문역사문화공원, 잠실역, 종합운동장, 삼성역, 선릉역, 교대역, 사당역, 당산역, 시청역);
    }

    @DisplayName("이미 등록된 구간은 등록할 수 없다.")
    @Test
    void addExistSection() {
        assertThatThrownBy(() -> sections.addSection(new Section(line, 교대역, 선릉역, 5)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(ALREADY_ADD_SECTION.getMessage());
    }

    @DisplayName("기존 구간이 존재할 때 상행역 하행역 모두 기존 구간에 존재하지 않으면 등록할 수 없다.")
    @Test
    void addNotExistUpStationAndDownStation() {
        assertThatThrownBy(() -> sections.addSection(
            new Section(line, new Station("합정역"), new Station("신촌역"), 5)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NOT_POSSIBLE_ADD_SECTION.getMessage());
    }

    @DisplayName("기존 구간 거리보다 크거나 같은 거리의 역은 등록할 수 없다.")
    @Test
    void addGreaterThanOrEqualDistanceStation() {
        Station 삼성역 = new Station("삼성역");
        assertThatThrownBy(() -> sections.addSection(
            new Section(line, 잠실역, 삼성역, 10)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(LESS_THAN_DISTANCE_BETWEEN_STATION.getMessage());

        assertThatThrownBy(() -> sections.addSection(
            new Section(line, 잠실역, 삼성역, 15)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(LESS_THAN_DISTANCE_BETWEEN_STATION.getMessage());

        assertDoesNotThrow(() -> sections.addSection(
            new Section(line, 잠실역, 삼성역, 9)));
    }

    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void removeSection() {
        // when
        sections.removeSection(잠실역);
        sections.removeSection(교대역);
        sections.removeSection(시청역);

        // then
        assertThat(sections.getStations()).containsExactly(선릉역, 당산역);
    }

    @DisplayName("지하철 구간이 한개인 경우 삭제할 수 없다.")
    @Test
    void removeLastSection() {
        // given
        Sections sections = new Sections();
        sections.add(new Section(line, 교대역, 당산역, 100));

        // when & then
        assertThatThrownBy(() -> sections.removeSection(교대역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NOT_REMOVE_SECTION_MIN_SIZE.getMessage());
    }

    @DisplayName("존재하지 않는 구간은 삭제할 수 없다.")
    @Test
    void removeNotExistSection() {
        // when & then
        assertThatThrownBy(() -> sections.removeSection(new Station("신촌역")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(NON_EXIST_STATION_TO_SECTION.getMessage());
    }
}
