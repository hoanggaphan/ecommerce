const localName = 'etoet-cart-history';
const storage = localStorage.getItem(localName);
const existing = storage ? JSON.parse(storage) : [];

export const addCartToStorage = (cart) =>
  localStorage.setItem(localName, JSON.stringify(cart));
export const getCartFromStorage = () => existing;
