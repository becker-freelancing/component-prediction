package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;

public class TokenizationService {

    private final HuggingFaceTokenizer tokenizer;
    private final TokenMapper tokenMapper;

    public TokenizationService(HuggingFaceTokenizer tokenizer, TokenMapper tokenMapper) {
        this.tokenizer = tokenizer;
        this.tokenMapper = tokenMapper;
    }

    public NDList encodeToNDList(NDManager manager, String text) {
        Encoding tokens = tokenizer.encode(text);
        NDArray inputIds = manager.create(tokens.getIds());
        NDArray attentionMask = manager.create(tokens.getAttentionMask());
        NDArray tokenTypeIds = manager.create(tokens.getTypeIds());
        return tokenMapper.apply(new Tokens(inputIds, attentionMask, tokenTypeIds));
    }
}
