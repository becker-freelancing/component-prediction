package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;

public class MeanPooler {

    public NDArray meanPool(NDList input) {
        if (input.size() != 1) {
            throw new IllegalArgumentException("Expected NDList with exactly 1 NDArray");
        }

        NDArray array = input.singletonOrThrow(); // [batch, seq_len, hidden_dim]
        // TODO Attention-Mask kann hier optional berücksichtigt werden
        return array.mean(new int[]{1}); // mittelt über seq_len -> [batch, hidden_dim]
    }

    public NDArray meanPool(NDArray hiddenStates, NDArray attentionMask) {
        NDArray mask = attentionMask.toType(hiddenStates.getDataType(), false);
        NDArray masked = hiddenStates.mul(mask.expandDims(2)); // [batch, seq_len, hidden_dim]
        NDArray sum = masked.sum(new int[]{1});
        NDArray count = mask.sum(new int[]{1}).clip(1e-9, Float.MAX_VALUE).expandDims(1);
        return sum.div(count); // Mittelwert ohne Padding
    }
}
