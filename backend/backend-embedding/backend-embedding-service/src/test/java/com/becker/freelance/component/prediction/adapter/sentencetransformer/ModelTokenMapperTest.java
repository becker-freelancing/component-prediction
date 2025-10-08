package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.Model;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.util.Pair;
import ai.djl.util.PairList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelTokenMapperTest {

    ModelTokenMapper modelTokenMapper;

    @BeforeEach
    void setUp() {
        Model model = Mockito.mock(Model.class);
        Mockito.doReturn(new PairList<>(List.of(
                new Pair<>("input_ids", new Shape(1)),
                new Pair<>("attention_mask", new Shape(2)),
                new Pair<>("token_type_ids", new Shape(3))
        ))).when(model).describeInput();
        modelTokenMapper = new ModelTokenMapper(model);
    }

    @Test
    void apply() {
        try (NDManager manager = NDManager.newBaseManager()) {

            NDArray ids = manager.create(new float[]{1, 2});
            NDArray attention = manager.create(new float[]{2, 3});
            NDArray types = manager.create(new float[]{3, 4});

            NDList actual = modelTokenMapper.apply(new Tokens(ids, attention, types));

            NDList expected = new NDList(ids, attention, types);

            assertEquals(expected, actual);
        }
    }


}