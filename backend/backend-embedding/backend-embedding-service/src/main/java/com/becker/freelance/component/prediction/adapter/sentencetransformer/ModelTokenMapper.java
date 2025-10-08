package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.Model;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.util.PairList;

public class ModelTokenMapper implements TokenMapper {

    private final PairList<String, Shape> inputDesc;

    public ModelTokenMapper(Model model) {
        this.inputDesc = model.describeInput();
    }

    @Override
    public NDList apply(Tokens tokens) {
        NDList input = new NDList();
        for (int i = 0; i < inputDesc.size(); i++) {
            String key = inputDesc.get(i).getKey();
            switch (key) {
                case "input_ids":
                    input.add(tokens.ids());
                    break;
                case "attention_mask":
                    input.add(tokens.attentionMask());
                    break;
                case "token_type_ids":
                    input.add(tokens.tokenTypeIds());
                    break;
                default:
                    throw new IllegalStateException("Unbekannter Input-Name im Modell: " + key);
            }
        }
        return input;
    }
}
