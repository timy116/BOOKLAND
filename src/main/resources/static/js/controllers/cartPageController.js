// 將書本從購物車中移除
async function controlCartItemRemove(url) {
  try {
    const result = await AJAX(url)
    console.log(result)

    cartPageView.updateCartItem()
    cartPageView.updateTotalPrice()
    cartPageView.updateCartNumber(result.cart ?? 0, false)

    // 判斷購物車是否為空
    if (!result.cart) cartPageView.resetPage()
  } catch (error) {
    console.log(error)
  }
}

async function controlCartItemUpdate(data, csrf) {
  try {
    const result = await AJAX(AJAX_CART_ITEM_UPDATE, data, csrf)
    console.log(result)

    if (result.cart) {
      cartPageView.updatePrice()
      cartPageView.updateTotalPrice()
    }
  } catch (error) {
    console.log(error)
  }
}

async function controlCheckout(hasShipping, hasInfo, set, inputMap) {
  const csrf = cartPageView.csrfToken
  const data = {}

  // 驗證寄件資訊是否有效
  for (let k of set) {
    console.log(k)
    if (k === cartPageView.name) {
      if (inputMap.get(k).trim().startsWith('請填寫')) {
        console.log('name')
        cartPageView.showErrMsg('請填寫收件人')
        cartPageView.deliverInfoInvalid(k)
        return
      }
      data['name'] = inputMap.get(k).trim()
    }
    if (k === cartPageView.phone) {
      if (inputMap.get(k).startsWith('請填寫')) {
        cartPageView.showErrMsg('請填寫聯絡電話')
        cartPageView.deliverInfoInvalid(k)
        return
      }
      if (new RegExp('[^\\d+]').exec(inputMap.get(k))) {
        cartPageView.showErrMsg('電話格式不正確')
        cartPageView.deliverInfoInvalid(k)
        return
      }
      data['phone'] = inputMap.get(k).trim()
    }
    if (k === cartPageView.address) {
      if (inputMap.get(k).startsWith('請填寫')) {
        console.log('name')
        cartPageView.showErrMsg('請填寫收件地址')
        cartPageView.deliverInfoInvalid(k)
        return
      }
      data['address'] = inputMap.get(k).trim()
    }
  }

  // 如果不存在寄送資訊，則發送 AJAX 到後端寫入資料庫
  if (!hasInfo) {
    try {
      const result = await AJAX(AJAX_USER_INFO_UPDATE, data, csrf)
      if (Number(result.status) !== 200)
        return
    } catch (e) {
      console.log(e)
    }
  }

  const stripe = Stripe(STRIPE_PUBLIC_KEY);
  try {
    const param = hasShipping ? {s: 1} : {s: 0}
    const session = await AJAX(AJAX_STRIPE_URL, param, csrf)
    const id = {sessionId: session.id};

    if (id)
      return stripe.redirectToCheckout(id);
    else
      window.location = HOME_URL

  } catch (error) {
    console.error("Error:", error);
  }
}

function init() {
  cartPageView.addHandlerCartItemRemove(controlCartItemRemove)
  cartPageView.addHandlerCartItemUpdate(controlCartItemUpdate)
  cartPageView.addHandlerCheckout(controlCheckout)
}

init()