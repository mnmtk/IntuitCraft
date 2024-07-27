package org.intuit.scoreservice.validators;

import org.intuit.scoreservice.models.entity.Player;

public interface PlayerValidator{
    public void validate(Player player) throws Exception;
}
