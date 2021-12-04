package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.fixture.SectionFixture.일호선_구간_강남역_신촌역;
import static nextstep.subway.line.domain.fixture.SectionFixture.일호선_구간_상행추가_신림역_강남역;
import static nextstep.subway.line.domain.fixture.SectionFixture.일호선_구간_신촌역_역삼역;
import static nextstep.subway.line.domain.fixture.SectionFixture.일호선_구간_역삼역_서울역;
import static nextstep.subway.line.domain.fixture.SectionFixture.일호선_구간_중간추가_신용산역_서울역;
import static nextstep.subway.line.domain.fixture.SectionFixture.일호선_구간_하행추가_서울역_용산역;
import static nextstep.subway.line.domain.fixture.SectionFixture.일호선_존재하지않는_용문역_양평역;
import static nextstep.subway.station.domain.StationFixture.강남역;
import static nextstep.subway.station.domain.StationFixture.서울역;
import static nextstep.subway.station.domain.StationFixture.신림역;
import static nextstep.subway.station.domain.StationFixture.신용산역;
import static nextstep.subway.station.domain.StationFixture.신촌역;
import static nextstep.subway.station.domain.StationFixture.역삼역;
import static nextstep.subway.station.domain.StationFixture.용산역;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SectionsTest {

    @Test
    void 첫번째역을_찾는다() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역(), 일호선_구간_신촌역_역삼역(), 일호선_구간_역삼역_서울역());

        // when
        Station upStation = 일호선_전체구간.findUpStation();

        // then
        Assertions.assertThat(upStation).isEqualTo(강남역());
    }

    @Test
    void 이미_등록된_구간_입니다() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역(), 일호선_구간_신촌역_역삼역(), 일호선_구간_역삼역_서울역());

        // then
        Assertions.assertThatThrownBy(() -> {
                일호선_전체구간.addLineStation(일호선_구간_신촌역_역삼역());
            }).isInstanceOf(RuntimeException.class)
            .hasMessageStartingWith("이미 등록된 구간 입니다.");
    }

    @Test
    void 등록할_수_없는_구간_입니다() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역(), 일호선_구간_신촌역_역삼역(), 일호선_구간_역삼역_서울역());

        // then
        Assertions.assertThatThrownBy(() -> {
                일호선_전체구간.addLineStation(일호선_존재하지않는_용문역_양평역());
            }).isInstanceOf(RuntimeException.class)
            .hasMessageStartingWith("등록할 수 없는 구간 입니다.");
    }

    @Test
    void 상행_구간_등록() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역(), 일호선_구간_신촌역_역삼역(), 일호선_구간_역삼역_서울역());

        // when
        일호선_전체구간.addLineStation(일호선_구간_상행추가_신림역_강남역());

        // then
        List<Station> stations = 일호선_전체구간.getStations();
        Assertions.assertThat(stations).containsExactly(신림역(), 강남역(), 신촌역(), 역삼역(), 서울역());
    }

    @Test
    void 중간_구간_등록() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역(), 일호선_구간_신촌역_역삼역(), 일호선_구간_역삼역_서울역());

        // given

        // when
        일호선_전체구간.addLineStation(일호선_구간_중간추가_신용산역_서울역());

        // then
        List<Station> stations = 일호선_전체구간.getStations();
        Assertions.assertThat(stations).containsExactly(강남역(), 신촌역(), 역삼역(), 신용산역(), 서울역());
    }

    @Test
    void 하행_구간_등록() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역(), 일호선_구간_신촌역_역삼역(), 일호선_구간_역삼역_서울역());

        // when
        일호선_전체구간.addLineStation(일호선_구간_하행추가_서울역_용산역());

        // then
        List<Station> stations = 일호선_전체구간.getStations();
        Assertions.assertThat(stations).containsExactly(강남역(), 신촌역(), 역삼역(), 서울역(), 용산역());
    }

    @Test
    void 전체구간이_하나인경우_삭제불가() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역());

        // then
        Assertions.assertThatThrownBy(() -> {
            일호선_전체구간.removeLineStation(강남역());
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    void 상행_구간_삭제() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역(), 일호선_구간_신촌역_역삼역(), 일호선_구간_역삼역_서울역());

        // when
        일호선_전체구간.removeLineStation(강남역());

        // then
        List<Station> stations = 일호선_전체구간.getStations();
        Assertions.assertThat(stations).containsExactly( 신촌역(), 역삼역(), 서울역());
    }

    @Test
    void 중간_구간_삭제() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역(), 일호선_구간_신촌역_역삼역(), 일호선_구간_역삼역_서울역());

        // when
        일호선_전체구간.removeLineStation(역삼역());

        // then
        List<Station> stations = 일호선_전체구간.getStations();
        Assertions.assertThat(stations).containsExactly(강남역(), 신촌역(),  서울역());
    }

    @Test
    void 하행_구간_삭제() {
        // given
        Sections 일호선_전체구간 = new Sections(일호선_구간_강남역_신촌역(), 일호선_구간_신촌역_역삼역(), 일호선_구간_역삼역_서울역());

        // when
        일호선_전체구간.removeLineStation(서울역());

        // then
        List<Station> stations = 일호선_전체구간.getStations();
        Assertions.assertThat(stations).containsExactly(강남역(), 신촌역(), 역삼역());
    }


}