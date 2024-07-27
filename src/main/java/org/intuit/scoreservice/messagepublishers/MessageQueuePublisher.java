package org.intuit.scoreservice.messagepublishers;

import org.intuit.scoreservice.models.entity.Score;

import java.util.concurrent.CompletableFuture;

public interface MessageQueuePublisher {
    CompletableFuture<Boolean> publish(Score Score);
}