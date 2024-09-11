var modelList;

async function loadProduct() {
    const response = await fetch('load-product', {
        method: 'GET',
    });
    if (!response.ok) {
        throw new Error("Error");
    }
    const data = await response.json();
    console.log(data)
    if (!data.success) {
        alert(data.message);
    }
    const category = data.message.category;
    const colors = data.message.colors;
    modelList = data.message.models;
    const condition = data.message.condition;
    const storage = data.message.storage;

    loadSelection('category-selector', category, ['id', 'name']);
    loadSelection('colors-selector', colors, ['id', 'name']);
    loadSelection('condition-selector', condition, ['id', 'name']);
    loadSelection('storage-selector', storage, ['id', 'value']);

}

function loadSelection(selectedId, list, propertyArray) {
    const selectTag = document.getElementById(selectedId);
    list.forEach((item) => {
        let option = document.createElement('option');
        option.value = item[propertyArray[0]];
        option.text = item[propertyArray[1]];
        selectTag.appendChild(option);
    });
}

function categoryChange() {
    let modelSelected = document.getElementById('model-selector');
    modelSelected.length = 0;
    let categorySelected = document.getElementById('category-selector').value;
    modelList.forEach((model) => {
        if (model.category.id == categorySelected) {
            let option = document.createElement('option');
            option.value = model.id;
            option.text = model.name;
            modelSelected.appendChild(option);
        }
    });
}

async function addProduct() {
    const categoryId = document.getElementById('category-selector').value;
    const modelId = document.getElementById('model-selector').value;
    const productTitle = document.getElementById('product-title').value;
    const productDescription = document.getElementById('product-description').value;
    const storageId = document.getElementById('storage-selector').value;
    const price = document.getElementById('product-price').value;
    const colorsId = document.getElementById('colors-selector').value;
    const conditionId = document.getElementById('condition-selector').value;
    const quantity = document.getElementById('product-quantity').value;
    const image1 = document.getElementById('product-image-1');
    const image2 = document.getElementById('product-image-2');
    const image3 = document.getElementById('product-image-3')

    let formData = new FormData();
    formData.append('categoryId', categoryId);
    formData.append('modelId', modelId);
    formData.append('productTitle', productTitle);
    formData.append('productDescription', productDescription);
    formData.append('storageId', storageId);
    formData.append('price', price);
    formData.append('colorsId', colorsId);
    formData.append('conditionId', conditionId);
    formData.append('quantity', quantity);
    formData.append('image1', image1.files[0]);
    formData.append('image2', image2.files[0]);
    formData.append('image3', image3.files[0]);

    const response = await fetch('list-product',
        {
            method: 'POST',
            body: formData
        });
}