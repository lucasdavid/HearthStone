package magic.core.actions;

import magic.core.Player;
import magic.core.actions.validation.ValidationRule;
import magic.core.actions.validation.rules.players.IsPlaying;
import magic.core.states.State;

import java.util.List;

/**
 * Disqualify a player from the game.
 *
 * @author ldavid
 */
public class DisqualifyAction extends Action {

    private final Player player;

    public DisqualifyAction(Player player) {
        this.player = player;
    }

    @Override
    public State update(State state) {
        List<State.PlayerState> players = state.playerStates();
        State.PlayerState p = state.playerState(player);

        players.set(
            players.indexOf(p),
            new State.PlayerState(p.player, p.life(), p.maxLife(),
                p.deck, p.hand, p.field, p.graveyard, false));

        return new State(players, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    protected ValidationRule validationRules() {
        return new IsPlaying(player);
    }
}