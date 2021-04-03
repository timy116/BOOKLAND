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