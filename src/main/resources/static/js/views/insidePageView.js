class InsidePageView extends CartView {
  _btnCart

  constructor() {
    super()
    this._btnCart = document.querySelector('.btn-cart')
    this._btnCart.style.transition = 'opacity 0.5s'
  }

  addHandlerToCart(handler) {
    this._btnCart.addEventListener('click', function (e) {
      if (!this.href.endsWith('cart')) {
        e.preventDefault()
        handler(this.href)
      }
    })
  }

  updateBtnCart() {
    this._btnCart.style.opacity = '0'
    setTimeout(function () {
      this._btnCart.style.opacity = '1'
      this._btnCart.href = '/cart'
      this._btnCart.innerText = '前往購物車'
    }.bind(this), 250)
  }
}

const insidePageView = new InsidePageView()
