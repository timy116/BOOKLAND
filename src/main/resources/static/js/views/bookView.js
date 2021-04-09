class BookView extends BaseView {
  _parentElement = document.querySelector('.container .row')
  _messageBox = document.querySelector('.message-box')
  _orderNumber = document.querySelector('.order-number')
  _timerDelay
  _timerMsgBox
  _delay = false

  addHandlerRender(handler) {
    const el = document.querySelector('.observer')
    const observer = new IntersectionObserver(handler, {
      root: null,
      threshold: 1,
    })

    observer.observe(el)
  }

  // 新增書本至購物車
  addHandlerToCart(handler) {
    this._parentElement.addEventListener('click', function (e) {
      if (!e.target.classList.contains('btn-cart')) return

      e.preventDefault()
      if (this._delay) return

      handler(e.target.href)
      this._delay = true
      console.log(e.target)
      if (!this._timerDelay) {
        console.log('set timer')
        this._timerDelay = setTimeout(
          function () {
            this._delay = false
            this._timerDelay = null
            console.log('clear timer', this._delay)
          }.bind(this)
          , 1500
        )
      }
    }.bind(this))
  }

  // 更新購物車數量
  updateCartNumber(quantity) {
    const el = this._orderNumber
    if (el.classList.contains('hide')) el.classList.remove('hide')
    el.innerText = quantity
    this._showMessageBox()
  }

  _showMessageBox() {
    if (this._timerMsgBox) clearTimeout(this._timerMsgBox)

    this._messageBox.style.opacity = '0.9'
    this._timerMsgBox = setTimeout(function () {
      this._messageBox.style.opacity = '0'
    }.bind(this), 2500)
  }

  // 用來輔助產生最終的 template string，此 method 會被多次呼叫
  _generateBookMarkup(book) {
    return `
      <div class="col-12 col-sm-6 col-md-4 col-lg-3">
        <div class="card">
          <img class="card-img-top" src="${book.imageUrl}" alt="${book.name}">
          <div class="card-body">
            <p class="card-text">${book.name}</p>
            <p><strong>NT ${book.price}</strong></p>
            <a href="/book/insidepage/${book.slug}" class="btn btn-outline-dark">more</a>
            <a href="/cart/add?id=${book.id}" class="btn btn-outline-danger btn-cart">Add to Cart</a>
          </div>
        </div>
      </div>
      `
  }

  // 產生最終要 render 的 template string
  _generateMarkup() {
    return this._data.map(result => this._generateBookMarkup(result)).join('')
  }
}

const bookView = new BookView()
