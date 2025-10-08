# Transformer-Model

````
pip install optimum onnxruntime onnx
optimum-cli export onnx --model sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2 ./embedding-model
````

Copy to `backend-embedding-service/embedding-model`

On Windows: Delete msvcp140.dll from Java installation
directory (https://github.com/microsoft/onnxruntime/discussions/23971)