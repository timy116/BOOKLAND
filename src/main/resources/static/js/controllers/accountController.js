async function controlShowOrder(orderNUmber) {
  try {
    const data = await AJAX(AJAX_ORDER.replace('{orderNumber}', orderNUmber))
    accountView.render(data)
  } catch (e) {
    console.log(e)
  }
}

function init() {
  accountView.addHandlerOrderModal(controlShowOrder)
}

init()