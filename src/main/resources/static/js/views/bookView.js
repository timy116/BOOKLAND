class BookView extends CartView {
  _parentElement = document.querySelector('.container .row')
  _messageBox = document.querySelector('.message-box')
  _spinner = document.querySelector('.load-bgc')
  _timerDelay
  _timerMsgBox
  _delay = false

  toggleSpinner() {
    this._spinner.classList.toggle('hide')
  }

  addHandlerRender(handler) {
    const el = document.querySelector('.observer')
    const observer = new IntersectionObserver(handler, {
      root: null,
      threshold: 1,
    })

    observer.observe(el)
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
