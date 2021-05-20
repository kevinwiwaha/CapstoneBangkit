async function loadImageData(){
    let image = document.getElementById('image')
    return tf.browser
    .fromPixels(image)
    .resizeNearestNeighbor([224, 224])
    .toFloat()
    .expandDims(); 
}