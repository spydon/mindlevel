package net.mindlevel.shared;

import java.io.Serializable;

public enum Category implements Serializable {
    ALL,
    ADVENTUROUS,
    ARTISTIC,
    FUNNY,
    KIND,
    OUTGOING;

    @Override
    public String toString() {
        return Normalizer.capitalizeName(name());
    }
}