import keras
from keras import backend as K
from keras.layers.core import Dense, Activation
from keras.optimizers import Adam
from keras.metrics import categorical_crossentropy
from keras.preprocessing.image import ImageDataGenerator
from keras.preprocessing import image
from keras.models import Model
from keras.applications import imagenet_utils
from keras.layers import Dense,GlobalAveragePooling2D
from keras.applications import MobileNet
from keras.applications.mobilenet import preprocess_input
import numpy as np
from IPython.display import Image
from keras.optimizers import Adam
import tensorboard
from datetime import datetime
from packaging import version
from tensorflow.keras.optimizers import RMSprop
from tensorflow import keras
import os

# Import Mobilenet
mobile = keras.applications.mobilenet.MobileNet()
print(mobile.summary())

# Freeze layers
x = mobile.get_layer('global_average_pooling2d')
output = Dense(units=1, activation='sigmoid')(x.output)
model = Model(inputs=mobile.input, outputs=output)
for i in model.layers[:-5]:
    i.trainable = False

# Image Data Generator
train_datagen=ImageDataGenerator(preprocessing_function=preprocess_input) 
train_generator=train_datagen.flow_from_directory('./DATASET/TRAIN', 
                                                 target_size=(224,224),
                                                 color_mode='rgb',
                                                 batch_size=32,
                                                 class_mode='binary',
                                                 shuffle=True)
                                                
test_datagen = ImageDataGenerator(rescale=1./255)
validation_generator = test_datagen.flow_from_directory(
                                                        './DATASET/TEST',
                                                        target_size=(150, 150),
                                                        batch_size=5,
                                                        class_mode='binary')

# Compile Model
model.compile(loss='binary_crossentropy',
              optimizer=RMSprop(lr=1e-4),
              metrics=['accuracy'])
# Fitting Model
step_size_train=train_generator.n//train_generator.batch_size
model.fit(train_generator,steps_per_epoch=2,epochs=5)

# Saving Model
model.save_weights("./model/model_weightsNew.h5")
model.save('./model/modelNew.h5')

