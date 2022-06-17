package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class SectionTest {

    @DisplayName("지하철 구간 생성")
    @Test
    void create_section() {
        // given
        Station upStation = Station.from("강남역");
        Station downStation = Station.from("역삼역");

        // when
        Section newSection = Section.of(upStation, downStation, 10);

        // then
        assertThat(newSection).isNotNull();
    }

    @DisplayName("기존 지하철 구간에 다른 구간을 병합한다.")
    @Test
    void merge_section() {
        // given
        Station upStation = Station.from("강남역");
        Station downStation = Station.from("역삼역");
        Section section = Section.of(upStation, downStation, 10);
        Station addedStation = Station.from("선릉역");
        Section addedSection = Section.of(downStation, addedStation, 5);

        // when
        Section mergeSection = section.mergeSection(addedSection);

        // then
        assertAll(
                () -> assertThat(mergeSection.isEqualsUpStation(downStation)),
                () -> assertThat(mergeSection.isEqualsDownStation(addedStation))
        );
    }

    @DisplayName("기존 지하철 구간을 업데이트 한다.")
    @Test
    void update_section() {
        // given
        Station upStation = Station.from("강남역");
        Station downStation = Station.from("역삼역");
        Section section = Section.of(upStation, downStation, 10);
        Station addedStation = Station.from("선릉역");
        Section addedSection = Section.of(upStation, addedStation, 5);

        // when
        section.updateSection(addedSection);

        // then
        assertAll(
                () -> assertThat(section.isEqualsUpStation(addedStation)),
                () -> assertThat(section.isEqualsDownStation(downStation))
        );
    }
}