class AccountView extends BaseView {
  _cardMap = new Map([
    ['visa', 'https://js.stripe.com/v3/fingerprinted/img/visa-365725566f9578a9589553aa9296d178.svg'],
    ['mastercard', 'https://js.stripe.com/v3/fingerprinted/img/mastercard-4d8844094130711885b5e41b28c9848f.svg'],
    ['amex', 'https://js.stripe.com/v3/fingerprinted/img/amex-a49b82f46c5cd6a96a6e418a6ca1717c.svg'],
  ])
  _parentElement = document.querySelector('.order-container')
  _modal = $('.order-modal')
  _orderBtn = document.querySelectorAll('.order-btn')

  addHandlerOrderModal(handler) {
    this._orderBtn.forEach(function (el) {
      el.addEventListener('click', function () {
        this.toggleSpinner()
        this._clear()
        handler(el.closest('.row-order').id)
        this._modal.modal('show')
        this.toggleSpinner()
      }.bind(this))
    }.bind(this))
  }

  // 用來輔助產生最終的 template string，此 method 會被多次呼叫
  _generateBookMarkup(book) {
    return `
      <tr class="bgcolor order-detail-item">
        <td><a href="/book/insidepage/${book.slug}">${book.name}</a></td>
        <td>${book.bookNumber}</td>
        <td>${book.price}</td>
        <td>${book.quantity}</td>
      </tr>
      `
  }

  _generateMarkup() {
    const order = this._data.order
    const card = order.creditCard
    const user = order.user
    const markup = `
    <div class="item-order">
      <div class="item-orderdetail">
        <div class="order">
          <p class="number">訂單編號：${order.orderNumber}</p>
        </div>
        <div class="order-items">
          <table class="table">
            <tbody>
              <tr>
                <th scope="row">訂單日期</th>
                <td class="order-date">${order.createTime}</td>
              </tr>
              <tr>
                <th scope="row">訂單狀態</th>
                <td>完成</td>
              </tr>
              <tr>
                <th scope="row">收件人姓名</th>
                <td class="order-name">${user.name}</td>
              </tr>
              <tr>
                <th scope="row">收件人電話</th>
                <td class="order-phone">${user.phone}</td>
              </tr>
              <tr>
                <th scope="row">收件人地址</th>
                <td class="order-address">${user.address}</td>
              </tr>
              <tr>
                <th scope="row">付款卡號</th>
                <td class="order-credit-card">
                <img style="width: 24px; height: 16px;" src="${this._cardMap.get(card.brand)}" alt="">
                <span>&nbsp;${String(card.last4).padStart(9, '•••• ')}</span>
              </td>
              </tr>
              <tr>
                <th scope="row">結帳金額</th>
                <td class="order-payment">${order.price}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="item-productdetail">
        <div class="pro-items">
          <p class="products">商品明細</p>
        </div>
        <div class="products-detail">
          <table class="table">
            <thead>
              <tr class="titcolor">
                <th scope="col">書名</th>
                <th scope="col">書號</th>
                <th scope="col">單價</th>
                <th scope="col">數量</th>
              </tr>
            </thead>
            <tbody class="order-details">
              ${this._data.orderDetails.map(book => this._generateBookMarkup(book)).join('')}
            </tbody>
          </table>
        </div>
      </div>
    </div>
    `
    return markup
  }
}

const accountView = new AccountView()
