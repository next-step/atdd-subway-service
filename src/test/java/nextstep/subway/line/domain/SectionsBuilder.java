package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

class SectionsBuilder {
    private List<Section> sectionList;

    private SectionsBuilder(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    SectionsBuilder addSection(Section section) {
        sectionList.add(section);
        return this;
    }

    Sections build() {
        Sections result = new Sections();
        for (Section section : sectionList) {
            result.addSection(section);
        }
        return result;
    }

    static SectionsBuilder aSections() {
        return new SectionsBuilder(new ArrayList<>());
    }
}
