package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.ndarray.NDList;

import java.util.function.Function;

public interface TokenMapper extends Function<Tokens, NDList> {
}
