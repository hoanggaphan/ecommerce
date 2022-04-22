import React from 'react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import ScrollToTop from './components/ScrollToTop';
import Category from './pages/Category';
import TopBarProgress from 'react-topbar-progress-indicator';

const Home = React.lazy(() => import('./pages/Home'));
const Cart = React.lazy(() => import('./pages/Cart'));
const Product = React.lazy(() => import('./pages/Product'));
const ProductList = React.lazy(() => import('./pages/ProductList'));
const Register = React.lazy(() => import('./pages/Register'));
const Login = React.lazy(() => import('./pages/Login'));

const queryClient = new QueryClient();

TopBarProgress.config({
  barColors: {
    0: 'teal',
    '1.0': 'teal',
  },
  shadowBlur: 5,
});

const App = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <ScrollToTop />
        <React.Suspense fallback={<TopBarProgress />}>
          <Routes>
            <Route path='/' element={<Home />} />
            <Route path='product/:slug' element={<Product />} />
            <Route path='category/:slug' element={<Category />} />
            <Route path='product-list' element={<ProductList />} />
            <Route path='register' element={<Register />} />
            <Route path='login' element={<Login />} />
            <Route path='cart' element={<Cart />} />
          </Routes>
        </React.Suspense>
      </BrowserRouter>
    </QueryClientProvider>
  );
};

export default App;
