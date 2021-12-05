package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Station 강남역;
    private Station 판교역;
    private Station 정자역;
    private Station 광교역;

    private Section 강남_판교_구간;
    private Section 정자_광교_구간;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");
        광교역 = new Station("광교역");

        강남_판교_구간 = Section.of(null, 강남역, 판교역, 5);
        정자_광교_구간 = Section.of(null, 정자역, 광교역, 10);
    }

    @Test
    void test_구간_추가_확인() {
        Sections sections = new Sections();
        sections.addSection(강남_판교_구간);
        sections.addSection(정자_광교_구간);

        assertAll(
            () -> assertThat(sections.getSections()).hasSize(2),
            () -> assertThat(sections.getSections().get(0).getUpStation().getName()).isEqualTo(강남역.getName()),
            () -> assertThat(sections.getSections().get(0).getDownStation().getName()).isEqualTo(판교역.getName()),
            () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(5)
        );
    }
}