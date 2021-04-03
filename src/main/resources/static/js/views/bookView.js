class BookView extends BaseView {
  _parentElement = document.querySelector('.container .row')

  addHandlerRender(handler) {
    const el = document.querySelector('.observer')
    const observer = new IntersectionObserver(handler, {
      root: null,
      threshold: 1,
    })

    observer.observe(el)
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
            <button type="button" class="btn btn-outline-dark">more</button>
            <button type="button" class="btn btn-outline-danger">Add to Cart</button>
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
