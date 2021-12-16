package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Lines {
  private final List<Line> lines;

  private Lines(List<Line> lines) {
    this.lines = lines;
  }

  public static Lines of(List<Line> lines) {
    return new Lines(lines);
  }

  public int getMaxSurcharge(List<Station> stations) {
    List<Line> surchargeLines = new ArrayList<>();
    for (Line line : lines) {
      addSurchargeLine(stations, surchargeLines, line);
    }

    return surchargeLines.stream()
            .mapToInt(Line::getSurcharge)
            .max()
            .orElse(0);
  }

  private void addSurchargeLine(List<Station> stations, List<Line> surchargeLines, Line line) {
    if (line.hasSection(stations)) {
      surchargeLines.add(line);
    }
  }

  public List<Line> toList() {
    return lines;
  }
}
