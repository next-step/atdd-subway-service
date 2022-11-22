package nextstep.subway.line.domain;

import java.util.List;

public class SectionsTestFixture {

    public static Sections createSections(List<Section> sections) {
        return Sections.from(sections);
    }
}
