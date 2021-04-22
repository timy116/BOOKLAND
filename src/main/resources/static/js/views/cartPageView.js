class CartPageView extends CartView {
  _parentElement = document.querySelector('.cart-item-container')
  _currentQty
  _currentItemEl
  _toRemoveEl
  _spinnerEl = document.querySelector('.load-bgc')
  _totalPriceEl = document.querySelector('.total-price .money span')
  _orderPriceEl = document.querySelector('.order-price .money span')
  _cartEmptyEl = document.querySelector('.cart-empty')
  _discountEl = document.querySelector('.cart-discount')
  _quantityNum = document.querySelectorAll('.input-qty')
  _cartItemRemoveBtn = document.querySelectorAll('.remove-btn')
  _checkoutBtn = document.querySelector('.btn-checkout')
  _deliverInfoBtn = document.querySelectorAll('.btn.btn-danger')

  constructor() {
    super();
    this._addHandlerDeliverInfoBtn()
  }

  // 寄送資訊 button 事件註冊
  _addHandlerDeliverInfoBtn() {
    this._deliverInfoBtn.forEach(function (el) {
      el.addEventListener('click', function () {
        const suffix = this.dataset.target.slice(1)
        const text = document.querySelector(`.text-${suffix}`)
        const input = document.querySelector(`.input-${suffix}`)

        if (input.value.trim().length !== 0) {
          text.innerHTML = input.value
          text.classList.remove('text-danger')
          input.value = ''
        }

      })
    })
  }

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
      const csrf = this.csrfToken
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
        }, csrf)
      })
    }.bind(this))
  }

  // 結帳按鈕事件註冊
  addHandlerCheckout(handler) {
    this._checkoutBtn.addEventListener('click', () => {
      // 如果沒登入，則導向登入頁面
      if (!document.querySelector('.user-none')) {
        window.location.href = LOGIN_URL
      }

      // 載入 spinner
      this._spinnerEl.classList.remove('hide')

      // 判斷 deliver info 是否已存在於資料庫(後端會先註記)
      const hasInfo = Boolean(+document.querySelector('.info').dataset.info)

      // 判斷是否要運費
      const hasShipping = this._discountEl.style.visibility === 'hidden' ? 1 : 0

      // 讀取 content 並驗證
      handler(
        hasShipping,
        hasInfo,
        new Set([this.name, this.phone, this.address]),
        new Map([
          [this.name, document.querySelector(this.name).innerHTML],
          [this.phone, document.querySelector(this.phone).innerHTML],
          [this.address, document.querySelector(this.address).innerHTML],
        ])
      )
    })
  }

  get csrfToken() {
    return document.querySelector(`meta[name="_csrf"]`)?.getAttribute('content');
  }

  get name() {
    return '.input.name';
  }

  get phone() {
    return '.input.phone';
  }

  get address() {
    return '.input.address';
  }

  // 寄送資訊錯誤提示
  deliverInfoInvalid(input) {
    this._spinnerEl.classList.add('hide')

    // 根據螢幕尺寸決定移動到哪
    let el
    if (window.innerWidth <= 420)
      el = '.strong-deliver-info'
    else if (window.innerWidth < 992)
      el = '.strong-cart'
    else if (window.innerWidth >= 992)
      el = 'header'

    // 動畫效果
    document.querySelector('.info').style.animation = ''
    document.querySelector(el).scrollIntoView({behavior: 'smooth'})
    document.querySelector('.info').style.animation = 'blink 0.1s step-end 14 alternate'
    document.querySelector(input).classList.add('text-danger')
  }

  updateCartItem() {
    // 將該書本從購物車中移除
    this._parentElement.removeChild(this._toRemoveEl)
  }

  // 更新書本金額
  updatePrice() {
    const bookPriceEl = this._currentItemEl.querySelector('.book-price')
    const qtyEl = this._currentItemEl.querySelector('.input-qty')
    const priceEl = this._currentItemEl.querySelector('.item-price')
    priceEl.innerText = +qtyEl.value * +bookPriceEl.innerHTML
  }

  // 更新總金額
  updateTotalPrice() {
    let total = 0
    document.querySelectorAll('.item-price').forEach(el => total += +el.innerText)
    this._totalPriceEl.innerText = total

    // 判斷是否免運
    if (total > 1999) {
      this._orderPriceEl.innerText = total
      this._discountEl.style.visibility = 'visible'
    } else {
      this._orderPriceEl.innerText = total + 80
      this._discountEl.style.visibility = 'hidden'
    }
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
