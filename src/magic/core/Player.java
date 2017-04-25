package magic.core;

import magic.core.states.IGameEventListener;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Player Base Class.
 * <p>
 * Basic interface for a Hearth Stone's player.
 * Check its sub-classes.
 */
public abstract class Player implements IIdentifiable, IGameEventListener {

    public static final Logger LOG = Logger.getLogger(Player.class.getName());

    public static final String[] DEFAULT_PLAYER_NAMES = new String[]{
        "Jane", "John", "Michael", "Lilliam", "Hellen", "Gus", "Josephine", "Richard",
        "Kyle", "Wendy", "Lucas", "Barbara", "Joe", "Ceres", "Pat", "Maximilian", "Maria",
        "Meredith", "Tully", "Tath", "Ted", "Nathan", "Norton", "Olaf", "Olga", "Patrick",
        "Parker", "Ruth", "Rick", "Rosa", "Rudy", "Rupert", "Sully", "Sigfried", "Simon",
        "Silvia", "Cecilia", "Ursula", "Usla"};

    private static int instantiatedCount = 0;
    private final UUID id;
    private final String name;

    protected Player(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(String name) {
        this(UUID.randomUUID(), name);
    }

    public Player() {
        this(DEFAULT_PLAYER_NAMES[(instantiatedCount++) % DEFAULT_PLAYER_NAMES.length]);
    }

    @Override
    public UUID id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        try {
            return id.equals(((Player) o).id);
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }
}