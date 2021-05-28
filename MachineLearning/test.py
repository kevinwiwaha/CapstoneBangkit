import tensorflow as tf
from keras.preprocessing import image
import matplotlib.pyplot as plt
import numpy as np
model = tf.keras.models.load_model('./model/model-test.h5')
# model.summary()

path = './src/images/kaleng.jpg'
img = image.load_img(path, target_size=(224, 224))
x = image.img_to_array(img)
plt.imshow(x/255.)
x = np.expand_dims(x, axis=0)
images = np.vstack([x])
print(np.array(images))
