package nextstep.subway.line.domain;

public interface SectionDeleteStrategy {
    void delete(Line line, Section sectionOfUpStation, Section sectionOfDownStation);
}
