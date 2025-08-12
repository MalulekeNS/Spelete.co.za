function search() {
  let input = document.getElementById('searchbar').value.toLowerCase();
  let resultsContainer = document.getElementById('search-results');
  let productItems = document.querySelectorAll('.product-item');
  resultsContainer.innerHTML = '';

  if (!input) {
    resultsContainer.style.display = 'none';
    return;
  }

  let results = [];
  productItems.forEach(item => {
    let name = item.querySelector('.product-info h4').textContent.toLowerCase();
    let category = item.querySelector('.product-info p.category').textContent.toLowerCase();
    if (name.includes(input) || category.includes(input)) {
      results.push({
        name: item.querySelector('.product-info h4').textContent,
        category: item.querySelector('.product-info p.category').textContent,
        link: item.querySelector('.details-link').href
      });
    }
  });

  if (results.length > 0) {
    results.forEach(result => {
      let resultItem = document.createElement('div');
      resultItem.className = 'result-item';
      resultItem.innerHTML = `<a href="${result.link}">${result.name} (${result.category})</a>`;
      resultsContainer.appendChild(resultItem);
    });
    resultsContainer.style.display = 'block';
  } else {
    resultsContainer.innerHTML = '<div class="result-item">No products found</div>';
    resultsContainer.style.display = 'block';
  }
}

document.addEventListener('click', function(event) {
  let searchbar = document.getElementById('searchbar');
  let resultsContainer = document.getElementById('search-results');
  if (!searchbar.contains(event.target) && !resultsContainer.contains(event.target)) {
    resultsContainer.style.display = 'none';
  }
});

function addToCart(productId, name, price, image) {
  let cart = JSON.parse(localStorage.getItem('cart')) || [];
  let product = cart.find(p => p.id === productId);
  if (product) {
    product.quantity += 1;
  } else {
    cart.push({ id: productId, name, price, image, quantity: 1 });
  }
  localStorage.setItem('cart', JSON.stringify(cart));
  updateCartCount();
  alert(`${name} added to cart!`);
}

function updateCartCount() {
  let cart = JSON.parse(localStorage.getItem('cart')) || [];
  let count = cart.reduce((sum, item) => sum + item.quantity, 0);
  document.getElementById('cart-count').textContent = count;
}

function showLoginModal() {
  let modal = new bootstrap.Modal(document.getElementById('loginModal'));
  modal.show();
}

document.addEventListener('DOMContentLoaded', () => {
  updateCartCount();
  fetch('get_products.php')
    .then(response => response.json())
    .then(products => {
      let productList = document.getElementById('product-list');
      products.forEach(product => {
        let productItem = document.createElement('div');
        productItem.className = `col-lg-4 col-md-6 product-item filter-${product.category}`;
        productItem.innerHTML = `
          <img src="${product.image}" class="img-fluid" alt="${product.name}">
          <div class="product-info">
            <h4>${product.name}</h4>
            <p class="category">${product.category.charAt(0).toUpperCase() + product.category.slice(1)}</p>
            <p>R${product.price.toFixed(2)}</p>
            <a href="product.php?id=${product.id}" class="details-link" title="View Details"><i class="bx bx-link"></i></a>
            <button class="btn btn-primary" onclick="addToCart(${product.id}, '${product.name}', ${product.price}, '${product.image}')">Add to Cart</button>
          </div>
        `;
        productList.appendChild(productItem);
      });

      // Initialize Isotope for filtering
      let portfolioContainer = document.querySelector('.portfolio-container');
      let portfolioIsotope = new Isotope(portfolioContainer, {
        itemSelector: '.product-item',
        layoutMode: 'fitRows'
      });

      let portfolioFilters = document.querySelectorAll('#portfolio-flters li');
      portfolioFilters.forEach(filter => {
        filter.addEventListener('click', function(e) {
          e.preventDefault();
          portfolioFilters.forEach(el => el.classList.remove('filter-active'));
          this.classList.add('filter-active');
          portfolioIsotope.arrange({
            filter: this.getAttribute('data-filter')
          });
        });
      });
    });
});