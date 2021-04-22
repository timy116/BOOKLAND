function addHandlerFacebook(handler) {
  const csrf = document.querySelector('.login-form input')

  document.querySelector('.fb-btn').addEventListener('click', function () {
    const markup = `
      <form method="post" action="/signin/facebook" class="fb-form" style="display:none;">
        <input type="hidden" name="_csrf" value="${csrf.value}">
        <input type="hidden" name="scope" value="public_profile">
      </form>
    `
    document.querySelector('footer').insertAdjacentHTML('beforeend', markup)
    handler(document.querySelector('.fb-form'))
  })
}

class LoginAndRegisterView {
  _formElement
  _btnSubmit

  constructor(formEl) {
    this._formElement = formEl
    this._btnSubmit = this._formElement.querySelector('.submit')
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

  // 顯示 AJAX 表單錯誤樣式
  renderAJAXFormError(errors) {
    if (errors.u) {
      const el = this._formElement.querySelector('#reg-username')
      el.classList.add('is-invalid')
      el.parentElement.querySelector('.form-text').innerText = '* 此帳號名稱已被使用'
    }
    if (errors.e) {
      const el = this._formElement.querySelector('#email')
      el.classList.add('is-invalid')
      el.parentElement.querySelector('.form-text').innerText = '* 此 email 已被使用'
    }
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

  getInspectQuery() {
    return [
      // 轉成 Base64
      btoa(this._formElement.querySelector('#reg-username').value.trim()),
      btoa(this._formElement.querySelector('#email').value.trim()),
    ]
  }
}

const loginView = new LoginAndRegisterView(document.querySelector('.login-form'))
const registerView = new LoginAndRegisterView(document.querySelector('.reg-form'))
