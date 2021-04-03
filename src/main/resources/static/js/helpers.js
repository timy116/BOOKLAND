// 請求超時中斷函數
function timeout(s) {
  return new Promise(function (_, reject) {
    setTimeout(function () {
      reject(new Error(`請求時間過長(${s} 秒)。`))
    }, s * 1000)
  })
}

/**
 * AJAX 請求通用函數
 *
 * @param url: AJAX 請求網址
 * @param formData: POST 傳輸資料
 * @return data: JSON 資料
 */
async function AJAX(url, formData = undefined) {
  try {
    const fetchPro = formData ? fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData),
    }) : fetch(url)

    const res = await Promise.race([fetchPro, timeout(TIMEOUT_SEC)])
    const data = await res.json()

    if (!res.ok) throw new Error(`${data.message} (${res.status})`)
    return data
  } catch (error) {
    throw error
  }
}