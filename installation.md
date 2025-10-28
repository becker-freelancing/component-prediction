# Transformer-Model

Currently only tested on Python version `3.11.9` with following instructions on Windows 11!

Important: This does not mean that other configurations or versions are not possible!

The tested model can be downloaded with this link `https://drive.google.com/drive/folders/1BgH3e4UAZ_pTpeMr5hQHll7raNo897A8` or created with:

````
# Required Libraries

pip install torch==2.3.1+cpu torchvision==0.18.1+cpu torchaudio==2.3.1+cpu --index-url https://download.pytorch.org/whl/cpu
pip install optimum==1.21.4 transformers==4.43.3 onnx onnxruntime
pip install -U sentence-transformers

# Model export to Onnx

optimum-cli export onnx --model sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2 ./embedding-model
````

Copy to `/models/paraphrase-multilingual-MiniLM-L12-v2` unter `resources` of `backend-embedding-service`.

On Windows: Delete msvcp140.dll from Java installation directory (https://github.com/microsoft/onnxruntime/discussions/23971)