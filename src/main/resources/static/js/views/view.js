class BaseView {
  _data
  _spinner = document.querySelector('.load-bgc')

  toggleSpinner() {
    this._spinner.classList.toggle('hide')
  }

  render(data, render = true, where = 'beforeend') {
    if (!data) return

    this._data = data
    const makeup = this._generateMarkup()
    if (!render) return makeup

    this._parentElement.insertAdjacentHTML(where, makeup)
  }

  _clear() {
    this._parentElement.innerHTML = ''
  }
}
