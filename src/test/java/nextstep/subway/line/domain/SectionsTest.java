package nextstep.subway.line.domain;

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
        assertThrows(RuntimeException.class, () ->
            sections.addSection(new Section(line, 교대역, 선릉역, 5)));
    }

    @DisplayName("기존 구간이 존재할 때 상행역 하행역 모두 기존 구간에 존재하지 않으면 등록할 수 없다.")
    @Test
    void addNotExistUpStationAndDownStation() {
        assertThrows(RuntimeException.class, () ->
            sections.addSection(
                new Section(line, new Station("합정역"), new Station("신촌역"), 5)));
    }
}
