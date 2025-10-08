package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.ndarray.NDArray;

public class MeanPooler {

    public NDArray meanPool(NDArray input) {

        return input.mean(new int[]{1}); // mean of seq_len -> [batch, hidden_dim]
    }
}
