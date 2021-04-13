// 展示書籍，捲軸快到底部時，會加載頁面繼續往下延伸(一次增加 12 本)，
// 直到所有書籍都展示出來
async function controlBook(entries, observer) {
  const [entry] = entries

  if (!entry.isIntersecting) return
  console.log(entry)
  paginator.page += 1

  // 取得網址列並且做切片
  const list = window.location.href.split('/')

  // 得到書籍的類別名稱
  const category = list[list.length - 1]

  try {
    bookView.toggleSpinner()
    const data = await loadBook(category)

    // 若資料長度小於 12，代表已經是最後一頁
    if (data.length < BOOK_SIZE_PER_PAGE) {
      if (data.length !== 0) {
        // 如果長度不為零，則做最後一次的 render
        bookView.render(data)
        bookView.toggleSpinner()
      }

      observer.unobserve(entry.target)
      return
    }
    bookView.render(data)
    bookView.toggleSpinner()
  } catch (error) {
    console.log(error)
  }

}

async function controlAddToCart(url) {
  try {
    const result = await AJAX(url)
    console.log(result)

    if(result.cart) {
      bookView.updateCartNumber(result.cart)
    }

  } catch (error) {
    console.log(error)
  }
}

function init() {
  bookView.addHandlerRender(controlBook)
  bookView.addHandlerToCart(controlAddToCart)
}

init()