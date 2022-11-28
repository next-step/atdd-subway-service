package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    @DisplayName("구간 추가")
    @Test
    void add() {

        Sections sections = new Sections();
        sections.add(SectionFixture.sectionAB());
        sections.add(SectionFixture.sectionBC());

        assertThat(sections.size()).isEqualTo(2);
    }

    @DisplayName("구간 역 목록 조회 시 하나도 없을 경우 emptyList 를 반환한다.")
    @Test
    void getStation() {

        Sections sections = new Sections();

        assertThat(sections.getStations()).isEqualTo(Collections.emptyList());
    }
}
