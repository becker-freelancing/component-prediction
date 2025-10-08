package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.ndarray.NDArray;

import java.util.Objects;

public record Tokens(NDArray ids, NDArray attentionMask, NDArray tokenTypeIds) {

    public Tokens {
        Objects.requireNonNull(ids, "ids must not be null");
        Objects.requireNonNull(attentionMask, "attentionMask must not be null");
        Objects.requireNonNull(tokenTypeIds, "tokenTypeIds must not be null");
    }

}
