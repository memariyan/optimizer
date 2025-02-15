package com.memariyan.optimizer.client.publisher;

import com.memariyan.optimizer.api.msg.response.OptimizationResponse;

public interface ResultPublisher {

    void publish(OptimizationResponse result, String receiverId);

}
