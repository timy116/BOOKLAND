const paginator = {
  page: 1,
}

async function loadBook(category) {
  try {
    return await AJAX(
      AJAX_BOOK_URL
        .replace("{category}", category)
        .replace("{num}", paginator.page)
    )
  } catch (error) {
    console.log(error)
    throw error
  }
}

async function addToCart(url) {
  try {
    return await AJAX(url)
  } catch (error) {
    console.log(error)
    throw error
  }
}