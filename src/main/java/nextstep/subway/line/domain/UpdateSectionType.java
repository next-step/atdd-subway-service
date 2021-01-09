package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Arrays;

public enum UpdateSectionType {

    UP {
        @Override
        protected boolean updatable(Section section, Station upStation, Station downStation) {
            return section.matchUpStation(upStation);
        }

        @Override
        public void updateSection(Section section, Station upStation, Station downStation, int distance) {
            section.updateUpStation(downStation, distance);
        }
    },

    DOWN {
        @Override
        protected boolean updatable(Section section, Station upStation, Station downStation) {
            return section.matchDownStation(downStation);
        }

        @Override
        public void updateSection(Section section, Station upStation, Station downStation, int distance) {
            section.updateDownStation(upStation, distance);
        }
    },

    NONE {
        @Override
        protected boolean updatable(Section section, Station upStation, Station downStation) {
            return false;
        }

        @Override
        public void updateSection(Section section, Station upStation, Station downStation, int distance) {}
    };

    public static UpdateSectionType valueOf(Section section, Station upStation, Station downStation) {
        return Arrays.stream(values())
                .filter(updateSectionType -> updateSectionType.updatable(section, upStation, downStation))
                .findFirst()
                .orElse(NONE);
    }

    protected abstract boolean updatable(Section section, Station upStation, Station downStation);
    public abstract void updateSection(Section section, Station upStation, Station downStation, int distance);
}
