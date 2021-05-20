import zipfile
import os
os.system('kaggle datasets download -d techsash/waste-classification-data')
with zipfile.ZipFile('./waste-classification-data.zip', 'r') as zip_ref:
    zip_ref.extractall('./')