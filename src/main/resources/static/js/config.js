const DOMAIN = window.location.href.split('/').slice(0, 3).join('/')
const TIMEOUT_SEC = 10
const BOOK_SIZE_PER_PAGE = 12
const AJAX_BOOK_URL = '/book/{category}/page{num}'
const AJAX_USER_INSPECT_URL = '/inspect?u={username}&e={email}'
const AJAX_STRIPE_URL = '/checkout'
const AJAX_CART_ITEM_UPDATE = '/cart/update'
const AJAX_ORDER = '/account/order-list?orderNumber={orderNumber}'
const AJAX_USER_INFO_UPDATE = '/user-info-update'
const STRIPE_PUBLIC_KEY = 'pk_test_51IGa8mDX8hkyYJgMQoiUkbmuVA6IZlvlp4rmDFM7dDFQKAY1DaFvcnng08xC1ZB31W2xUTx7KeEaLFV0glWG8Gbe00J7nQSQiT'
const LOGIN_URL = `${DOMAIN}/login?next=checkout`
const HOME_URL = DOMAIN
const MSG_SEC = 1800
