package nextstep.subway.line.domain;

import nextstep.exception.StationNotConnectedException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class SectionsTest {

    private static final Line 이호선 = new Line("2호선", "green");
    private static final Station 강남역 = new Station("강남역");
    private static final Station 교대역 = new Station("교대역");
    private static final Station 역삼역 = new Station("역삼역");
    private static final Station 삼성역 = new Station("삼성역");
    private static final Station 잠실역 = new Station("잠실역");

    @Test
    void getStations_역들을_조회한다() {
        // given
        Sections sections = new Sections();
        sections.add(new Section(이호선, 삼성역, 잠실역, 20));
        sections.add(new Section(이호선, 교대역, 강남역, 10));
        sections.add(new Section(이호선, 역삼역, 삼성역, 30));
        sections.add(new Section(이호선, 강남역, 역삼역, 40));

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(교대역, 강남역, 역삼역, 삼성역, 잠실역);
    }

    @Test
    void updateUpStation_상행역을_업데이트한다() {
        // given
        Sections sections = getSections();

        // when
        sections.updateUpStation(강남역, 역삼역, 3);

        // then
        assertThat(sections.getSections()).contains(new Section(이호선, 역삼역, 삼성역, 7));
    }

    @Test
    void updateDownStation_하행역을_업데이트한다() {
        // given
        Sections sections = getSections();

        // when
        sections.updateDownStation(역삼역, 삼성역, 3);

        // then
        assertThat(sections.getSections()).contains(new Section(이호선, 강남역, 역삼역, 7));
    }

    @Test
    void hasStation_역이_있는지_학인한다() {
        // given
        Sections sections = getSections();

        // when
        boolean hasStation = sections.hasStation(강남역);

        // then
        assertThat(hasStation).isTrue();
    }

    @Test
    void updateStation_상행역을_업데이트한다() {
        // given
        Sections sections = getSections();

        // when
        sections.updateStation(역삼역, 삼성역, 3);

        // then
        assertThat(sections.getSections()).contains(new Section(이호선, 강남역, 역삼역, 7));
    }

    @Test
    void updateStation_하행역을_업데이트한다() {
        // given
        Sections sections = getSections();

        // when
        sections.updateStation(강남역, 역삼역, 3);

        // then
        assertThat(sections.getSections()).contains(new Section(이호선, 역삼역, 삼성역, 7));
    }

    @Test
    void checkUpdatable_이미_등록된_구간인_경우_에러를_발생한다() {
        // given
        Sections sections = getSections();

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sections.checkUpdatable(강남역, 삼성역));
    }

    @Test
    void checkUpdatable_등록할_수_없는_구간인_경우_에러를_발생한다() {
        // given
        Sections sections = getSections();

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sections.checkUpdatable(교대역, 잠실역));
    }

    @Test
    void findUpSection_상행구간을_찾는다() {
        // given
        Sections sections = getSections();

        // when
        Optional<Section> upStation = sections.findUpSection(강남역);

        // then
        assertThat(upStation.isPresent()).isTrue();
        assertThat(upStation.get()).isEqualTo(new Section(이호선, 강남역, 삼성역, 10));
    }

    @Test
    void findDownSection_하행구간을_찾는다() {
        // given
        Sections sections = getSections();

        // when
        Optional<Section> upStation = sections.findDownSection(삼성역);

        // then
        assertThat(upStation.isPresent()).isTrue();
        assertThat(upStation.get()).isEqualTo(new Section(이호선, 강남역, 삼성역, 10));
    }

    @Test
    void remove_구간을_제거한다() {
        // given
        Sections sections = getSections();

        // when
        sections.remove(new Section(이호선, 강남역, 삼성역, 10));

        // then
        assertThat(sections.getSections()).isEmpty();
    }

    @Test
    void isRemovable_삭제_가능여부를_체크한다() {
        // given
        Sections sections = getSections();

        // when
        boolean isRemovable = sections.isRemovable();

        // then
        assertThat(isRemovable).isFalse();
    }

    @Test
    void checkRemovable_삭제_불가능하면_에러를_발생한다() {
        // given
        Sections sections = getSections();

        // when
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sections.checkRemovable());
    }

    @Test
    void getAllStations_구간의_모든_역들을_조회한다() {
        // given
        Sections sections = getSections();

        // when
        List<Station> stations = sections.getAllStations();

        // then
        assertThat(stations).containsExactly(강남역, 삼성역);
    }

    @Test
    void checkConnected_출발역과_도착역이_연결되지_않으면_에러를_발생한다() {
        // given
        Sections sections = getSections();

        // when
        assertThatExceptionOfType(StationNotConnectedException.class)
                .isThrownBy(() -> sections.checkConnected(강남역, 교대역));
    }

    private Sections getSections() {
        Sections sections = new Sections();
        sections.add(new Section(이호선, 강남역, 삼성역, 10));
        return sections;
    }
}
