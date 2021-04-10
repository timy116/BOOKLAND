class CartPageView extends CartView {
  _toRemoveEl
  _parentElement = document.querySelector('.cart-item-container')
  _cartItemRemoveBtn = document.querySelectorAll('.remove-btn')
  _cartEmptyEl = document.querySelector('.cart-empty')
  _quantityNum = document.querySelectorAll('.input-qty')

  // 所有購物車刪除按鈕註冊事件
  addHandlerCartItemRemove(handler) {
    const self = this
    this._cartItemRemoveBtn.forEach(function (el) {
      el.addEventListener('click', function (e) {
        e.preventDefault()
        handler(el.href)
        self._toRemoveEl = el.closest('.cart-item')
      })
    })
  }

  updateCartItem() {
    // 將該書本從購物車中移除
    this._parentElement.removeChild(this._toRemoveEl)
  }

  // 購物車為空時，將某些元素顯示/隱藏
  resetPage() {
    this._orderNumber.classList.add('hide')
    this._parentElement.classList.add('hide')
    this._cartEmptyEl.classList.remove('hide')
  }
}

const cartPageView = new CartPageView()
