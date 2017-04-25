package magic.core.actions;

import magic.core.actions.validation.ValidationRule;
import magic.core.actions.validation.rules.TurnsStepIs;
import magic.core.actions.validation.rules.players.ActiveAndTurnsPlayersAreTheSame;
import magic.core.actions.validation.rules.players.active.CardsCountIsLessThan;
import magic.core.cards.Cards;
import magic.core.cards.ICard;
import magic.core.states.State;
import magic.core.states.State.PlayerState;
import magic.core.states.TurnStep;

import java.util.List;

import static magic.core.actions.validation.ValidationRules.And;
import static magic.core.actions.validation.ValidationRules.Not;

/**
 * Discard Action.
 * <p>
 * Current player discards a card from their hand.
 *
 * @author ldavid
 */
public final class DiscardAction extends Action {

    private final ICard card;

    public DiscardAction(ICard card) {
        this.card = card;
    }

    @Override
    public State update(State state) {
        List<PlayerState> playersInfo = state.playerStates();
        PlayerState p = playersInfo.remove(state.turnsPlayerIndex);

        List<ICard> hand = p.hand.cards();
        List<ICard> graveyard = p.graveyard.cards();

        graveyard.add(hand.remove(hand.indexOf(card)));

        playersInfo.add(state.turnsPlayerIndex,
            new PlayerState(p.player, p.life(), p.maxLife(),
                p.deck, new Cards(hand), p.field, new Cards(graveyard)));

        return new State(playersInfo, state.turn, state.step, state.done,
            state.turnsPlayerIndex, state.activePlayerIndex, this, state);
    }

    @Override
    protected ValidationRule validationRules() {
        return And(
            new ActiveAndTurnsPlayersAreTheSame(),
            new TurnsStepIs(TurnStep.CLEANUP),
            Not(new CardsCountIsLessThan(8)));
    }
}
