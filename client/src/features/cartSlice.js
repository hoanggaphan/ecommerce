import { createSlice } from '@reduxjs/toolkit';
import { addCartToStorage, getCartFromStorage } from '../utils/localStorage';

const calculateCartAmount = () =>
  getCartFromStorage().reduce((prev, curr) => prev + curr.amount, 0);

const initialState = {
  cart: getCartFromStorage(),
  length: calculateCartAmount(),
};

export const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    addToCart: (state, action) => {
      const index = state.cart.findIndex(
        (item) => item.skuId === action.payload.skuId
      );

      let newCart;
      if (index !== -1) {
        newCart = [...state.cart];
        newCart[index].amount += action.payload.amount;
      } else {
        newCart = [...state.cart, action.payload];
      }

      addCartToStorage(newCart);
      state.cart = newCart;
      state.length = newCart.reduce((prev, curr) => prev + curr.amount, 0);
    },
    increaseDecreaseAmount: (state, action) => {
      const index = state.cart.findIndex(
        (item) => item.skuId === action.payload.skuId
      );

      if (index !== -1) {
        const newCart = [...state.cart];
        newCart[index].amount = action.payload.amount;

        addCartToStorage(newCart);
        state.cart = newCart;
        state.length = newCart.reduce((prev, curr) => prev + curr.amount, 0);
      }
    },
    removeItemFromCart: (state, action) => {
      const index = state.cart.findIndex(
        (item) => item.skuId === action.payload.skuId
      );
      console.log(index);
      if (index !== -1) {
        const newCart = [...state.cart];
        newCart.splice(index, 1);

        addCartToStorage(newCart);
        state.cart = newCart;
        state.length = newCart.reduce((prev, curr) => prev + curr.amount, 0);
      }
    },
    clearCart: (state) => {},
  },
});

export const {
  addToCart,
  increaseDecreaseAmount,
  removeItemFromCart,
  clearCart,
} = cartSlice.actions;

export default cartSlice.reducer;
