# Introduction
Welcome to the Android App that classifies images of dogs and cats using a `PyTorch`. The app is designed to help users easily classify images of dogs and cats with the touch of a button.

| <img src="https://github.com/kumresh/CatDogClassification/blob/master/app/src/main/assets/loading.jpeg" width="240">   | <img src="https://github.com/kumresh/CatDogClassification/blob/master/app/src/main/assets/predict.jpeg" width="240"> |
|:---:|:---:|

# Features
- Easy and intuitive image classification of `dogs` and `cats`
- Accurate image recognition using a `PyTorch` model
- Minimal and user-friendly interface

## How to Use
- Open the app
- Choose an image from your device's gallery or take a new photo
- Press the "Pridict" button
The app will display the classification result (either "dog" or "cat")

## How to reduce prediction Time
- **Optimize the Model**: Try to optimize the model by reducing its size, number of parameters and layer complexity. 
This will help in reducing the computational time during predictions.
- **Quantization**: Use quantization techniques to reduce the model size and make predictions faster. 
PyTorch Lite supports quantization-aware training, which helps to optimize the model for deployment on low-power devices.
- **Model Input Shape**: Ensure that the input shape of the model is optimized for the Android device to reduce the computational time during predictions.

# Technical Details
The app uses a pre-trained PyTorch for image classification. The model has been trained on a large dataset of dog and cat images, resulting in high accuracy and speed.

## Conclusion
I hope you find this app useful and enjoyable. With its fast and accurate image recognition, you can now easily classify images of dogs and cats with just a few taps. Enjoy!




