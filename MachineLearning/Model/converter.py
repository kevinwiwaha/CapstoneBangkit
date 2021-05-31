import os
# Convert to TFJS
os.system('tensorflowjs_converter --input_format=keras --output_format=tfjs_layers_model ./model/modelNew.h5 ./json-model')
                   