# Ingredients Classification Model

## Description
The **Ingredients Classification Model** is a deep learning model based on **MobileNetV3Large** designed to classify kitchen ingredients such as spices, fruits, or vegetables from images. It is lightweight, efficient, and suitable for deployment in various applications.

---

## Features
- **Backbone**: MobileNetV3Large (pre-trained on ImageNet).
- **Data Augmentation**:
  - Random flipping (horizontal and vertical).
  - Random zoom, rotation, brightness, and contrast adjustments.
- **Input Size**: 224x224 RGB images.
- **Output**: Multi-class classification with softmax activation.

---

### Dataset Splits
- **Training**: 80% of the data.
- **Validation**: 10% (subset of training).
- **Testing**: 10%.

---

## Preprocessing
1. Images resized to **224x224**.
2. Pixel values normalized to the range **[0, 1]**.
3. Augmented with random transformations:
   - Flip, zoom, rotation, contrast, brightness, and more.

---

## Model Architecture
1. **Base Model**:
   - Pre-trained MobileNetV3Large with frozen weights.
2. **Custom Layers**:
   - Dense layers with ReLU activation and L2 regularization.
   - Batch normalization and dropout for regularization.
   - Final softmax layer for multi-class classification.

---

## Training
### Hyperparameters
- **Optimizer**: Adam (initial learning rate: `1e-4`).
- **Loss Function**: Categorical Crossentropy.
- **Batch Size**: 32.
- **Epochs**: 100 (with early stopping).
- **Callbacks**:
  - EarlyStopping: Stops if validation accuracy doesn't improve for 10 epochs.
  - ModelCheckpoint: Saves the best weights based on validation accuracy.
  - LearningRateScheduler: Adjusts learning rate after 20 epochs.

---

## Evaluation
The model is evaluated on the test set:
- **Metrics**:
  - Accuracy.
  - Loss.

Example results:
- **Test Loss**: 2.35649.
- **Test Accuracy**: 87.30%.

---
