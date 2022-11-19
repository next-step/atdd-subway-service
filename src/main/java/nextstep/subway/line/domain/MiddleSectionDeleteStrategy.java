package nextstep.subway.line.domain;

public class MiddleSectionDeleteStrategy implements SectionDeleteStrategy {
    @Override
    public void delete(Line line, Section sectionOfUpStation, Section sectionOfDownStation) {
        sectionOfDownStation.extendSection(sectionOfUpStation);
        line.deleteSection(sectionOfUpStation);
    }
}
