package org.jmagic.observers;

import org.jmagic.actions.Action;
import org.jmagic.actions.AdvanceGame;
import org.jmagic.actions.Disqualify;
import org.jmagic.core.cards.Cards;
import org.jmagic.core.states.State;
import org.jmagic.infrastructure.validation.rules.ValidationRule;
import org.jmagic.players.RandomPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

class LooseOnIllegalActionAttemptTest {

    private final State INITIAL_STATE = new State(
            List.of(new RandomPlayer("test-1"), new RandomPlayer("test-2")),
            List.of(Cards.EMPTY, Cards.EMPTY),
            0);

    @Test
    void afterPlayerAct() {
        Observer o = new LooseOnIllegalActionAttempt(
                AdvanceGame.class,
                LegalAction.class);

        State actual = o.afterPlayerAct(INITIAL_STATE, new LegalAction(), 0, 100);
        assertEquals(INITIAL_STATE, actual);

        actual = o.afterPlayerAct(INITIAL_STATE, new IllegalAction(), 0, 100);
        assertNotEquals(INITIAL_STATE, actual);
        assertTrue(actual.actionThatLedToThisState instanceof Disqualify);
    }

    private static class LegalAction extends Action {

        @Override
        public State update(State state) {
            return state;
        }

        @Override
        public ValidationRule validationRules() {
            return null;
        }
    }

    private static class IllegalAction extends LegalAction {

        @Override
        public State update(State state) {
            return state;
        }

        @Override
        public ValidationRule validationRules() {
            return null;
        }
    }

}