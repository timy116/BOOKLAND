const listCart = document.querySelector('.list-cart')

if (!listCart.querySelector('.order-number')) {
  const markup = `
  <p class="order-number hide"></p>
  `
  listCart.insertAdjacentHTML('afterbegin', markup)
}