package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class L2NormalizerTest {

    @Test
    void normalizeIfDisabled() {
        L2Normalizer normalizer = new L2Normalizer(false);

        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray ndArray = manager.create(new float[]{1, 2, 3});

            NDArray normalized = normalizer.normalizeIfEnabled(ndArray);

            assertEquals(ndArray, normalized);
        }
    }


    @Test
    void normalizeIfEnabled() {
        L2Normalizer normalizer = new L2Normalizer(true);

        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray ndArray = manager.create(new float[]{2, 3, 6});

            NDArray normalized = normalizer.normalizeIfEnabled(ndArray);

            NDArray expected = manager.create(new float[]{2f / 7, 3f / 7, 6f / 7});
            assertEquals(expected, normalized);
        }
    }

}