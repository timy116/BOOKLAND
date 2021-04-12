class CartPageView extends CartView {
  _parentElement = document.querySelector('.cart-item-container')
  _currentQty
  _currentItemEl
  _toRemoveEl
  _totalPriceEl = document.querySelector('.total-price .money span')
  _orderPriceEl = document.querySelector('.order-price .money span')
  _cartEmptyEl = document.querySelector('.cart-empty')
  _quantityNum = document.querySelectorAll('.input-qty')
  _cartItemRemoveBtn = document.querySelectorAll('.remove-btn')
  _checkoutBtn = document.querySelector('.btn-checkout')

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

  addHandlerCartItemUpdate(handler) {
    const self = this
    this._quantityNum.forEach(function (el) {
      // 一進入 input 時立刻存下當前數量
      el.addEventListener('focus', function () {
        self._currentQty = el.value
      })
      el.addEventListener('blur', function () {
        if (+el.value <= 0) el.value = 1
        if (self._currentQty === el.value) return

        self._currentItemEl = el.closest('.cart-item')

        // AJAX post data
        handler({
          id: self._currentItemEl.id.split('-')[2],
          qty: +el.value,
        })
      })
    })
  }

  addHandlerCheckout(handler) {
    if (window.location.href.indexOf('checkout') !== -1) {
      document.querySelector('body').style.display = 'none'
      handler()
    }

    this._checkoutBtn.addEventListener('click', () => {
      if (!document.querySelector('.user-none')) {
        window.location.href = LOGIN_URL
      }

      document.querySelector('.container').style.visibility = 'hidden'
      document.querySelector('.load-bgc').classList.remove('hide')
      handler()
    })
  }

  updateCartItem() {
    // 將該書本從購物車中移除
    this._parentElement.removeChild(this._toRemoveEl)
  }

  // 更新書本金額
  updatePrice() {
    const bookPriceEl = this._currentItemEl.querySelector('.book-price b')
    const qtyEl = this._currentItemEl.querySelector('.input-qty')
    const priceEl = this._currentItemEl.querySelector('.item-price b')
    priceEl.innerText = +qtyEl.value * +bookPriceEl.innerHTML
  }

  // 更新總金額
  updateTotalPrice() {
    let total = 0
    document.querySelectorAll('.item-price b').forEach(el => total += +el.innerText)
    this._totalPriceEl.innerText = total
    this._orderPriceEl.innerText = total + 80
  }

  // 購物車為空時，將某些元素顯示/隱藏
  resetPage() {
    this._orderNumber.classList.add('hide')
    this._parentElement.classList.add('hide')
    this._cartEmptyEl.classList.remove('hide')
    this._totalPriceEl.innerText = ''
    this._orderPriceEl.innerText = ''
  }
}

const cartPageView = new CartPageView()
