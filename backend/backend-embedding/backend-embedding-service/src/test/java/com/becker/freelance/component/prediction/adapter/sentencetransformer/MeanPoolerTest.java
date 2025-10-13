package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MeanPoolerTest {

    MeanPooler meanPooler;

    @BeforeEach
    void setUp() {
        meanPooler = new MeanPooler();
    }


    @Test
    void meanPool() {
        try (NDManager ndManager = NDManager.newBaseManager()) {
            NDArray arr1 = ndManager.create(new float[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}});
            NDArray result = meanPooler.meanPool(arr1);

            NDArray expected = ndManager.create(new float[]{5, 6, 7, 8});

            assertEquals(expected, result);
        }
    }


}