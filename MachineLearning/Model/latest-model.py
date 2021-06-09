import tensorflow as tf
from tensorflow.keras.applications.resnet50 import ResNet50
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Flatten
from tensorflow.keras.layers import Dense
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import os


model = Sequential()
model.add(MobileNetV2(include_top=False,
          weights="imagenet", input_shape=(224, 224, 3)))
model.add(tf.keras.layers.GlobalAveragePooling2D())
model.add(tf.keras.layers.Dropout(0.2))
model.add(tf.keras.layers.Dense(512, activation='relu'))
model.add(tf.keras.layers.Dense(64, activation='relu'))
model.add(tf.keras.layers.Dense(8, activation='relu'))
model.add(Dense(1, activation='sigmoid'))
model.layers[0].trainable = False

model.summary()

train_datagen = ImageDataGenerator(
    preprocessing_function=tf.keras.applications.mobilenet.preprocess_input)
train_generator = train_datagen.flow_from_directory('./DATASET/TRAIN',
                                                    target_size=(224, 224),
                                                    color_mode='rgb',
                                                    batch_size=120,
                                                    class_mode='binary',
                                                    shuffle=True)


test_datagen = ImageDataGenerator(rescale=1./255)
validation_generator = test_datagen.flow_from_directory(
    './DATASET/TEST',
    target_size=(150, 150),
    batch_size=120,
    class_mode='binary')

model.compile(loss='binary_crossentropy',
              optimizer=tf.keras.optimizers.RMSprop(learning_rate=1e-4),
              metrics=['accuracy'])

callback = tf.keras.callbacks.EarlyStopping(monitor='loss', patience=3)
step_size_train = train_generator.n//train_generator.batch_size
model.fit(train_generator, steps_per_epoch=5, epochs=5,
          validation_data=validation_generator, callbacks=[callback])
