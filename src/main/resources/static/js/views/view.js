class BaseView {
  _data

  render(data, render = true) {
    if (!data || (Array.isArray(data) && data.length === 0)) return this.renderError()

    this._data = data
    const makeup = this._generateMarkup()
    if (!render) return makeup

    this._clear()
    this._parentElement.insertAdjacentHTML('beforeend', makeup)
  }

  renderSpinner() {
    const markup = ``
    this._clear()
    this._parentElement.insertAdjacentHTML('afterbegin', markup)
  }

  _clear() {
    this._parentElement.innerHTML = ''
  }
}
