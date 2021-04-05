// login form validate object
const constraints = {
  'username': {
    presence: {
      message: '^* 此欄位不可空白'
    },
  },
  'password': {
    presence: {
      message: '^* 此欄位不可空白'
    }
  },
}

// register form validate object
const regConstraints = {
  'reg-username': {
    presence: {
      message: '^* 此欄位不可空白'
    },
    format: {
      pattern: '[\\w]+',
      message: '^* 只能輸入英文或數字'
    },
    length: {
      minimum: 4,
      tooShort: '^* 帳號至少 4 位英文或數字',
    },
  },
  'password1': {
    presence: {
      message: '^* 此欄位不可空白'
    },
    length: {
      minimum: 6,
      tooShort: '^* 密碼至少 6 位以上',
    }
  },
  'password2': {
    presence: {
      message: '^* 此欄位不可空白'
    },
    equality: {
      attribute: 'password1',
      message: '^* 密碼不相符'
    }
  },
  'email': {
    presence: {
      message: '^* 此欄位不可空白'
    },
    email: {
      message: '^* Email 格式不正確'
    },
  }
}
