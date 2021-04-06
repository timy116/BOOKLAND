async function controlFormValidate(obj, form) {
  // 表單基本驗證
  const errors = validate(form, form.classList.contains('reg-form') ? regConstraints : constraints)
  console.log(obj.classList)

  // 若有欄位驗證失敗
  if(errors != null && Object.keys(errors).length !==0) {
    obj.renderFormError(errors)
    return
  }

  // 基本驗證若通過則驗證是否有重複資料
  if (form.classList.contains('reg-form')) {
    const errors = await userInspect(...obj.getInspectQuery())
    if (Object.keys(errors).length !==0) {
      obj.renderAJAXFormError(errors)
      return
    }
  }

  const el = `<input type="checkbox" name="remember-me" style="display: none;" checked>`
  form.insertAdjacentHTML('beforeend', el)
  form.submit()
}

function init() {
  registerView.addHandlerSubmit(controlFormValidate)
  loginView.addHandlerSubmit(controlFormValidate)
}

init()