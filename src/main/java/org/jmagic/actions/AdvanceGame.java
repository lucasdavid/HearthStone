package org.jmagic.actions;

import org.jmagic.actions.rules.game.TurnStepIs;
import org.jmagic.actions.rules.players.HasPerformedThisTurn;
import org.jmagic.actions.rules.players.IsTurnOfActivePlayer;
import org.jmagic.actions.rules.players.PlayersOtherThanActiveAreAlive;
import org.jmagic.actions.rules.players.active.HasFewerCardsInHandThan;
import org.jmagic.core.states.State;
import org.jmagic.core.states.TurnSteps;
import org.jmagic.infrastructure.validation.rules.ValidationRule;

import java.util.List;
import java.util.stream.Collectors;

import static org.jmagic.infrastructure.validation.rules.BasicRules.*;

/**
 * Advance Turn Action.
 * <p>
 * Action used by a player to advance turn.
 *
 * @author ldavid
 */
public class AdvanceGame extends Action {

    @Override
    public State update(State state) {
        List<State.PlayerState> players = state.playerStates();

        if (state.step == TurnSteps.UNTAP
            || state.step == TurnSteps.COMBAT_DAMAGE) {
            // Skip all other players when dealing untapping or calculating combat.

            // I know this for a fact, as the active and turn's player are the
            // same when the turn's step jumps forward.
            assert state.turnsPlayerIndex == state.activePlayerIndex;

            return new State(players, state.turn, state.step.next(), state.done,
                state.turnsPlayerIndex, state.turnsPlayerIndex, this, state);
        }

        List<Integer> alives = players.stream()
            .filter(State.PlayerState::isAlive)
            .map(players::indexOf)
            .collect(Collectors.toList());

        int lastPlayerIndex = alives.stream()
            .filter(i -> i < state.turnsPlayerIndex)
            .reduce((a, b) -> b)
            .orElse(alives.get(alives.size() - 1));

        if (state.activePlayerIndex == lastPlayerIndex) {
            // Last player has requested turn advancement.

            if (state.step == TurnSteps.CLEANUP || !state.turnsPlayerState().isAlive()) {
                // That was the last step of the turn's current player or they died.
                // Change turn's player to the next alive.
                int turnsPlayer = state.turnsPlayerIndex;
                do {
                    turnsPlayer = (turnsPlayer + 1) % players.size();
                } while (!state.playerState(turnsPlayer).isAlive());

                return new State(players, state.turn + 1, TurnSteps.UNTAP, state.done,
                    turnsPlayer, turnsPlayer);
            }

            // Go to next step of this turn.
            return new State(players, state.turn, state.step.next(), state.done,
                state.turnsPlayerIndex, state.turnsPlayerIndex, this, state);
        }

        // Change active player to the next alive.
        int activePlayerIndex = state.activePlayerIndex;
        do {
            activePlayerIndex = (activePlayerIndex + 1) % players.size();
        } while (!state.playerState(activePlayerIndex).isAlive());

        return new State(players, state.turn, state.step, state.done,
            state.turnsPlayerIndex, activePlayerIndex, this, state);
    }

    @Override
    public ValidationRule validationRules() {
        return And(
            new PlayersOtherThanActiveAreAlive(),
            Or(
                // Prevents players from advancing without initially drawing 7 cards.
                Not(new TurnStepIs(TurnSteps.INITIAL_DRAWING)),
                new HasPerformedThisTurn(InitialDraw.class)),
            Or(
                // Prevents players from advancing without drawing a card.
                Not(new TurnStepIs(TurnSteps.DRAW)),
                Not(new IsTurnOfActivePlayer()),
                new HasPerformedThisTurn(Draw.class)),
            Or(
                // Prevents advancing turn with more than 8 cards on a player's hand.
                Not(new TurnStepIs(TurnSteps.CLEANUP)),
                new HasFewerCardsInHandThan(8)));
    }

}
