import tensorflow as tf
from tensorflow import keras
#from keras import backend as K
#from tensorflow.keras.layers.core import Dense, Activation
#from keras.optimizers import Adam
#from keras.metrics import categorical_crossentropy
#from keras.preprocessing.image import ImageDataGenerator
#from keras.preprocessing import image
#from keras.models import Model
#from keras.applications import imagenet_utils
#from keras.layers import Dense,GlobalAveragePooling2D
#from keras.applications import MobileNet
#from keras.applications.mobilenet import preprocess_input
#import numpy as np
#from IPython.display import Image
#from keras.optimizers import Adam
#import tensorboard
#from datetime import datetime
#from packaging import version
#from tensorflow.keras.optimizers import RMSprop
#from tensorflow import keras
import os

# Import Mobilenet
mobile = tf.keras.applications.mobilenet.MobileNet()
print(mobile.summary())

# Freeze layers
x = mobile.get_layer('global_average_pooling2d')
output = tf.keras.layers.Dense(units=1, activation='sigmoid')(x.output)
model = tf.keras.Model(inputs=mobile.input, outputs=output)
for i in model.layers[:-5]:
    i.trainable = False

# Image Data Generator
train_datagen=tf.keras.preprocessing.image.ImageDataGenerator(preprocessing_function=tf.keras.applications.mobilenet.preprocess_input) 
train_generator=train_datagen.flow_from_directory('./DATASET/TRAIN', 
                                                 target_size=(224,224),
                                                 color_mode='rgb',
                                                 batch_size=120,
                                                 class_mode='binary',
                                                 shuffle=True)
                                                
test_datagen = tf.keras.preprocessing.image.ImageDataGenerator(rescale=1./255)
validation_generator = test_datagen.flow_from_directory(
                                                        './DATASET/TEST',
                                                        target_size=(150, 150),
                                                        batch_size=32,
                                                        class_mode='binary')

# Compile Model
model.compile(loss='binary_crossentropy',
              optimizer=tf.keras.optimizers.RMSprop(lr=1e-4),
              metrics=['accuracy'])
# Fitting Model
step_size_train=train_generator.n//train_generator.batch_size
model.fit_generator(train_generator,steps_per_epoch=step_size_train,epochs=100)

# Saving Model
model.save_weights("./model/model_weights-test.h5")
model.save('./model/model-test.h5')

