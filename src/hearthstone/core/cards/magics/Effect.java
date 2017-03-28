package hearthstone.core.cards.magics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class Effect extends Magic {

    public static Collection<Effect> DEFAULT_CARDS = Collections.unmodifiableCollection(Arrays.asList());

    public Effect(String name, int manaCost) {
        super(name, manaCost);
    }

    public Effect(UUID id, String name, int manaCost) {
        super(id, name, manaCost);
    }

    @Override
    public String toString() {
        return String.format("%s: effect (mana cost: %d)",
                getName(), getManaCost());
    }
}