function controlFormValidate(obj, form) {
  const errors = validate(form, form.classList.contains('reg-form') ? regConstraints : constraints)
  console.log(obj, errors)

  if(errors != null && Object.keys(errors).length !==0) {
    obj.renderFormError(errors)
    return
  }

}

function init() {
  registerView.addHandlerSubmit(controlFormValidate)
  loginView.addHandlerSubmit(controlFormValidate)
}

init()