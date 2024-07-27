package org.intuit.scoreservice.validators;

import org.intuit.scoreservice.exceptions.InvalidScoreException;
import org.intuit.scoreservice.models.entity.Score;
import org.modelmapper.ValidationException;

public interface ScoreValidator {
    void validate(Score score) throws Exception;
}