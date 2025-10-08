package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.Model;
import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.util.PairList;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        // Path to model folder
        Path modelDir = Paths.get(Main.class.getResource("/models/paraphrase-multilingual-MiniLM-L12-v2").toURI());

        // ONNX model path
        Path modelPath = modelDir.resolve("model.onnx");

        // HuggingFace tokenizer
        Path tokenizerPath = modelDir.resolve("tokenizer.json");
        HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance(tokenizerPath);

        // Example text
        String text = "Wo kann ich eine Form einfügen?";

        // Tokenize text
        Encoding tokens = tokenizer.encode(text);
        long[] tokenIds = tokens.getIds();              // [seq_len]
        long[] attentionMask = tokens.getAttentionMask(); // [seq_len]
        long[] tokenTypeIds = tokens.getTypeIds();       // [seq_len], usually zeros for single sentence

        NDManager manager = NDManager.newBaseManager();

        System.out.println("input_ids shape: " + manager.create(tokenIds).expandDims(0).getShape());
        System.out.println("attention_mask shape: " + manager.create(attentionMask).expandDims(0).getShape());
        System.out.println("token_type_ids shape: " + manager.create(tokenTypeIds).expandDims(0).getShape());


        // Load model
        try (Model model = Model.newInstance("embedding-model", "OnnxRuntime")) {
            model.load(modelPath);
            // nach model.load(modelPath);
            PairList<String, Shape> inputDesc = model.describeInput(); // PairList<String, Shape>
            System.out.println("Model input order:");
            for (int i = 0; i < inputDesc.size(); i++) {
                System.out.println(i + " -> " + inputDesc.get(i).getKey() + " : " + inputDesc.get(i).getValue());
            }

// Erzeuge 1D-NDArrays (keine Batch-Dimension!)
            NDArray idsArr = manager.create(tokenIds);           // shape [seq_len]
            NDArray maskArr = manager.create(attentionMask);     // shape [seq_len]
            NDArray typeIdsArr = manager.create(tokenTypeIds);   // shape [seq_len]

// Debug-Ausgabe: Shapes, die wirklich in die NDList kommen
            System.out.println("idsArr shape: " + idsArr.getShape());
            System.out.println("maskArr shape: " + maskArr.getShape());
            System.out.println("typeIdsArr shape: " + typeIdsArr.getShape());

// Baue NDList IN DER GENAUE N MODEL-INPUT-REIHENFOLGE (wie describeInput() angibt)
            NDList input = new NDList();
            for (int i = 0; i < inputDesc.size(); i++) {
                String key = inputDesc.get(i).getKey();
                switch (key) {
                    case "input_ids":
                        input.add(idsArr);
                        break;
                    case "attention_mask":
                        input.add(maskArr);
                        break;
                    case "token_type_ids":
                        input.add(typeIdsArr);
                        break;
                    default:
                        throw new IllegalStateException("Unbekannter Input-Name im Modell: " + key);
                }
                System.out.println("Added to NDList: " + key + " (index " + i + ")");
            }

// Noch ein Check: shapes in der NDList (sollten 1D sein)
            for (int i = 0; i < input.size(); i++) {
                System.out.println("NDList[" + i + "] shape = " + input.get(i).getShape());
            }

// Predict — DJL fügt nun die Batch-Dimension hinzu (-> ONNX sieht [batch, seq_len])
            try (Predictor<NDList, NDList> predictor = model.newPredictor(new Translator<NDList, NDList>() {
                @Override
                public NDList processOutput(TranslatorContext translatorContext, NDList ndList) throws Exception {
                    return ndList;
                }

                @Override
                public NDList processInput(TranslatorContext translatorContext, NDList ndArrays) throws Exception {
                    return ndArrays;
                }
            })) {
                NDList output = predictor.predict(input);
                NDArray raw = output.singletonOrThrow(); // flach, aber enthielt die Werte
                Shape rawShape = raw.getShape();
                System.out.println("Raw output shape: " + rawShape); // erwartung: (1, seq_len, dim) oder (seq_len, dim) etc.

                NDArray sentenceEmbedding;
                if (rawShape.dimension() == 3) {
                    // raw shape: [batch, seq_len, dim]
                    long batch = rawShape.get(0);
                    long seqLen = rawShape.get(1);
                    long dim = rawShape.get(2);

                    // attentionMask: long[] attentionMask (wie zuvor), für batch=1
                    NDArray mask = manager.create(attentionMask).reshape(batch, seqLen).toType(DataType.FLOAT32, false); // [batch, seq_len]
                    NDArray maskExp = mask.expandDims(2); // [batch, seq_len, 1]

                    // weighted sum over tokens
                    NDArray weighted = raw.toType(DataType.FLOAT32, false).mul(maskExp); // [batch, seq_len, dim]
                    NDArray summed = weighted.sum(new int[]{1}); // sum over seq_len -> [batch, dim]

                    // denom: number of valid tokens per sample
                    NDArray denom = mask.sum(new int[]{1}).reshape(batch, 1); // [batch, 1]
                    // avoid division by zero:
                    denom = denom.add(1e-9f);

                    sentenceEmbedding = summed.div(denom); // [batch, dim]
                } else if (rawShape.dimension() == 2) {
                    // raw is already [batch, dim] or [seq_len, dim] depending; handle accordingly
                    sentenceEmbedding = raw; // evtl reshape nötig
                } else {
                    throw new IllegalStateException("Unerwartete Output-Rank: " + rawShape.dimension());
                }

// L2-normalize (pro Batch-Eintrag)
                NDArray sq = sentenceEmbedding.pow(2).sum(new int[]{1}).sqrt().expandDims(1).add(1e-9f); // [batch,1]
                NDArray normalized = sentenceEmbedding.div(sq); // [batch, dim]

// get as float[] für batch=1
                float[] vector = normalized.squeeze().toFloatArray();
                System.out.println("Final embedding length: " + vector.length);
            }

        }
    }
}
