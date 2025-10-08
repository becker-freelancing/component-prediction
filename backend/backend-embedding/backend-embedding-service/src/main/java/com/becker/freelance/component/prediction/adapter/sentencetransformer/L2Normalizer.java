package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.types.DataType;

public class L2Normalizer {

    private final boolean enabled;

    public L2Normalizer(boolean enabled) {
        this.enabled = enabled;
    }

    public NDArray normalizeIfEnabled(NDArray array) {
        if (!enabled) return array;
        NDArray arrayFloat = array.toType(DataType.FLOAT32, false); // assert Float
        int axis = (array.getShape().dimension() == 1) ? 0 : 1; // Choose axis for sum
        NDArray norm = arrayFloat.abs().pow(2).sum(new int[]{axis}).sqrt().expandDims(axis).add(1e-9f);
        return arrayFloat.div(norm);
    }
}
