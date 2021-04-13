class CartView extends BaseView {
  _orderNumber = document.querySelector('.order-number')

  // 新增書本至購物車
  addHandlerToCart(handler) {
    this._parentElement.addEventListener('click', function (e) {
      if (!e.target.classList.contains('btn-cart')) return

      e.preventDefault()

      // 限制使用者連續觸發按鈕
      if (this._delay) return

      handler(e.target.href)
      this._delay = true

      // 每隔 1.5 秒後才能觸發按鈕後續發生事件
      if (!this._timerDelay) {
        this._timerDelay = setTimeout(
          function () {
            this._delay = false
            this._timerDelay = null
          }.bind(this)
          , 1500
        )
      }
    }.bind(this))
  }

  // 更新購物車數量
  updateCartNumber(quantity, hasMsg = true) {
    const el = this._orderNumber
    if (el.classList.contains('hide')) el.classList.remove('hide')
    el.innerText = quantity
    if (hasMsg) this.showMsg('已新增至購物車')
  }
}