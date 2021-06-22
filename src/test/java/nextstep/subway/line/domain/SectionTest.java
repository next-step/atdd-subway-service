package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {
    private Line 이호선;
    private Station 강남역;
    private Station 삼성역;
    private Station 선릉역;
    private Station 잠실역;
    private Section section;

    @BeforeEach
    void setUp() {
        //given
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        삼성역 = new Station("삼성역");
        ReflectionTestUtils.setField(삼성역, "id", 2L);
        선릉역 = new Station("선릉역");
        ReflectionTestUtils.setField(선릉역, "id", 3L);
        잠실역 = new Station("잠실역");
        ReflectionTestUtils.setField(잠실역, "id", 4L);
        이호선 = new Line("2호선", "green");
        ReflectionTestUtils.setField(이호선, "id", 1L);
        section = new Section(이호선, 강남역, 삼성역, 10);
        ReflectionTestUtils.setField(이호선, "sections", new Sections(new ArrayList<>(Collections.singletonList(section))));
    }

    @Test
    void updateUpStation() {
        //given
        Section newSection = new Section(이호선, 선릉역, 삼성역, 1);

        //when
        section.updateUpStation(newSection);

        //then
        assertThat(section.getDistance().getDistance()).isEqualTo(9);
        assertThat(section.getUpStation()).isEqualTo(newSection.getDownStation());
    }

    @Test
    void updateDownStation() {
        //given
        Section newSection = new Section(이호선, 선릉역, 삼성역, 1);

        //when
        section.updateDownStation(newSection);

        //then
        assertThat(section.getDistance().getDistance()).isEqualTo(9);
        assertThat(section.getDownStation()).isEqualTo(newSection.getUpStation());
    }

    @Test
    void isUpStationEqualsToStation() {
        //when
        boolean actual = section.isUpStationEqualsToStation(강남역);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void isDownStationEqualsToStation() {
        //when
        boolean actual = section.isDownStationEqualsToStation(삼성역);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void isUpStationEqualsToUpStationInSection() {
        //given
        Section newSection = new Section(이호선, 강남역, 선릉역, 1);

        //when
        boolean actual = this.section.isUpStationEqualsToUpStationInSection(newSection);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void isDownStationEqualsToDownStationInSection() {
        //given
        Section newSection = new Section(이호선, 잠실역, 삼성역, 1);

        //when
        boolean actual = this.section.isDownStationEqualsToDownStationInSection(newSection);

        //then
        assertThat(actual).isTrue();
    }
}