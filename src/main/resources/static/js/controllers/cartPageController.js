// 將書本從購物車中移除
async function controlCartItemRemove(url) {
  try {
    const result = await AJAX(url)
    console.log(result)

    cartPageView.updateCartItem()
    cartPageView.updateCartNumber(result.cart?? 0, false)

    // 判斷購物車是否為空
    if(!result.cart) cartPageView.resetPage()
  } catch (error) {
    console.log(error)
  }
}

function init() {
  cartPageView.addHandlerCartItemRemove(controlCartItemRemove)
}
init()