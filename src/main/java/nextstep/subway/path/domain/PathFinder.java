package nextstep.subway.path.domain;

public interface PathFinder {

    Path findShortest(final PathSections pathSections, final PathStation source, final PathStation target);
}
