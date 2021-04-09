async function controlAddToCart(url) {
  try {
    const result = await addToCart(url)
    console.log(result)

    if(result.cart) {
      insidePageView.updateCartNumber(result.cart, false)
      insidePageView.updateBtnCart()
    }

  } catch (error) {
    console.log(error)
  }
}

function init() {
  insidePageView.addHandlerToCart(controlAddToCart)
}

init()