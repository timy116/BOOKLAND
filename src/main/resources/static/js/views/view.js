/*
* 基礎 View 類別，通用的 attribute 與 method 都會放在這裡
* */
class BaseView {
  _data
  _spinner = document.querySelector('.load-bgc')
  _messageBox

  constructor() {
    this._generateMsgBox()
  }

  // 動態產生 Message Box 元素，預設會添加在 body 尾巴
  _generateMsgBox() {
    const markup = `
    <div class="message-box">
      <div>
        <i class="msg-icon"></i>
        <small class="inner-text" style="vertical-align: bottom;"></small>
      </div>
    </div>
    `

    document.querySelector('body').insertAdjacentHTML('beforeend', markup)
    this._messageBox = document.querySelector('.message-box')
  }

  // 顯示 Message Box
  _showMessageBox(err = false, message) {
    const el = this._messageBox
    const icon = el.querySelector('.msg-icon')
    if (this._timerMsgBox) clearTimeout(this._timerMsgBox)

    if (err) {
      icon.classList.add('bi-exclamation-circle')
      icon.classList.remove('bi-check-circle')
      el.style.backgroundColor = '#dc3545'
    } else {
      icon.classList.remove('bi-exclamation-circle')
      icon.classList.add('bi-check-circle')
      el.style.backgroundColor = '#28a745'
    }

    el.querySelector('.inner-text').innerText = `${message}`
    el.style.opacity = '0.9'

    this._timerMsgBox = setTimeout(function () {
      el.style.opacity = '0'
    }.bind(this), MSG_SEC)
  }

  // 顯示訊息
  showMsg(msg) {
    this._showMessageBox(undefined, msg)
  }

  // 顯示錯誤訊息
  showErrMsg(msg) {
    this._showMessageBox(true, msg)
  }

  // 切換 Spinner 效果
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
