package org.jmagic.core.cards;

import org.jmagic.core.cards.lands.BasicLands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public abstract class Card implements ICard {

    private final UUID id;
    private final String name;
    private final Collection<BasicLands> cost;
    private final Collection<Properties> properties;

    public Card(UUID id, String name, Collection<Properties> properties, Collection<BasicLands> cost) {
        this.id = id;
        this.name = name;
        this.properties = properties;
        this.cost = cost;
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
    public Collection<Properties> properties() {
        return new ArrayList<>(properties);
    }

    @Override
    public Collection<Properties> effectiveProperties() {
        return properties();
    }

    @Override
    public Collection<BasicLands> cost() {
        return new ArrayList<>(cost);
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this == o || id().equals(((Card) o).id());
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 2;
        hash = 17 * hash + Objects.hashCode(id());
        return hash;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    @Override
    public String toString(boolean detailed) {
        String description = detailed
            ? String.format("\"%s\" c:%s", name, cost)
            : String.format("\"%s\"", name);

        if (detailed && !properties().isEmpty()) {
            description += String.format(" p:%s", properties());
        }

        return description;
    }
}
