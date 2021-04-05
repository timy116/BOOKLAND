class LoginAndRegisterView {
  _formElement
  _btnSubmit

  constructor(formEl) {
    this._formElement = formEl
    this._btnSubmit = this._formElement.querySelector('button')
    this._addHandlerInput()
  }

  // 當欄位正在輸入時，移除錯誤訊息樣式
  _addHandlerInput() {
    this._formElement.querySelectorAll('input').forEach(el => {
      el.addEventListener('input', function () {
        if (this.classList.contains('is-invalid'))
          this.classList.remove('is-invalid')
      })
    })
  }

  addHandlerSubmit(handler) {
    this._btnSubmit.addEventListener('click', function () {
      handler(this, this._formElement)
    }.bind(this))
  }

  // 顯示表單錯誤樣式
  renderFormError(errors) {
    this._formElement.querySelectorAll('input').forEach(function (el) {
      // 判斷該欄位是否輸入錯誤
      if (errors[el.name]) {
        // 顯示 border
        el.classList.add('is-invalid')

        // 顯示錯誤訊息
        el.parentElement.querySelector('.form-text').innerText = errors[el.name][0]
      }
    })
  }
}

const loginView = new LoginAndRegisterView(document.querySelector('.login-form'))
const registerView = new LoginAndRegisterView(document.querySelector('.reg-form'))