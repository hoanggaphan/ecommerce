import { Box } from '@material-ui/core';
import IconButton from '@material-ui/core/IconButton';
import { Add, Close, Remove } from '@material-ui/icons';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router';
import styled from 'styled-components';
import Swal from 'sweetalert2';
import withReactContent from 'sweetalert2-react-content';
import Announcement from '../components/Announcement';
import Footer from '../components/Footer';
import Navbar from '../components/Navbar';
import {
  increaseDecreaseAmount,
  removeItemFromCart,
} from '../features/cartSlice';
import { mobile } from '../responsive';

const MySwal = withReactContent(Swal);

const Container = styled.div``;

const Wrapper = styled.div`
  padding: 20px;
  ${mobile({ padding: '10px' })}
`;

const Title = styled.h1`
  font-weight: 300;
  text-align: center;
`;

const Top = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
`;

const TopButton = styled.button`
  padding: 10px;
  font-weight: 600;
  cursor: pointer;
  border: ${(props) => props.type === 'filled' && 'none'};
  background-color: ${(props) =>
    props.type === 'filled' ? 'black' : 'transparent'};
  color: ${(props) => props.type === 'filled' && 'white'};
`;

const TopTexts = styled.div`
  ${mobile({ display: 'none' })}
`;
const TopText = styled.span`
  font-weight: bold;
  font-size: 20px;
  margin: 0px 10px;
`;

const Bottom = styled.div`
  display: flex;
  column-gap: 30px;
  justify-content: space-between;
  ${mobile({ flexDirection: 'column' })}
`;

const Info = styled.div`
  flex: 3;
`;

const Product = styled.div`
  display: flex;
  justify-content: space-between;
  ${mobile({ flexDirection: 'column' })}
  position: relative;
`;

const ProductDetail = styled.div`
  flex: 2;
  display: flex;
`;

const Image = styled.img`
  width: 200px;
`;

const Details = styled.div`
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
`;

const ProductName = styled.span``;

const ProductId = styled.span``;

const ProductColor = styled.span``;

const ProductSize = styled.span``;

const PriceDetail = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const ProductAmountContainer = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 20px;
`;

const ProductAmount = styled.div`
  font-size: 24px;
  margin: 5px;
  ${mobile({ margin: '5px 15px' })}
`;

const ProductPrice = styled.div`
  font-size: 30px;
  font-weight: 200;
  ${mobile({ marginBottom: '20px' })}
`;

const Hr = styled.hr`
  background-color: #eee;
  border: none;
  height: 1px;
`;

const Summary = styled.div`
  flex: 1;
  border: 0.5px solid lightgray;
  border-radius: 10px;
  padding: 20px;
  height: 50vh;
`;

const SummaryTitle = styled.h1`
  font-weight: 200;
`;

const SummaryItem = styled.div`
  margin: 30px 0px;
  display: flex;
  justify-content: space-between;
  font-weight: ${(props) => props.type === 'total' && '500'};
  font-size: ${(props) => props.type === 'total' && '24px'};
`;

const SummaryItemText = styled.span``;

const SummaryItemPrice = styled.span``;

const CloseButton = styled.div`
  position: absolute;
  top: 0;
  right: 0;
`;

const Button = styled.button`
  cursor: pointer;
  width: 100%;
  padding: 10px;
  font-weight: 600;

  border: ${(props) => props.type === 'filled' && 'none'};
  background-color: ${(props) =>
    props.type === 'filled' ? 'black' : 'transparent'};
  color: ${(props) => props.type === 'filled' && 'white'};
`;

const Cart = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const cart = useSelector((state) => state.cart.cart);
  const cartLength = useSelector((state) => state.cart.length);
  const total = cart.reduce(
    (prev, curr) => prev + curr.amount * curr.basePrice,
    0
  );
  const shipCost = cartLength <= 0 ? 0 : total > 50000 ? 0 : 50000;

  const handleAmount = (skuId, amount) => {
    if (amount < 1 || amount > 99) return;
    dispatch(increaseDecreaseAmount({ skuId, amount }));
  };

  const handleRemove = (skuId) => {
    MySwal.fire({
      title: 'Xóa sản phẩm',
      text: 'Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng của bạn',
      showCancelButton: true,
      confirmButtonText: 'Xóa',
      cancelButtonText: 'Hủy',
      confirmButtonColor: 'teal',
    }).then((res) => {
      if (res.isConfirmed) {
        dispatch(removeItemFromCart({ skuId }));
      }
    });
  };

  return (
    <Container>
      <Navbar />
      <Announcement />
      <Wrapper>
        <Title>GIỎ HÀNG</Title>
        <Top>
          <TopButton onClick={() => navigate('/')}>TRANG CHỦ</TopButton>
          <TopTexts>
            <TopText>TỔNG ĐƠN HÀNG | {cartLength} SẢN PHẨM</TopText>
          </TopTexts>
          <TopButton type='filled'>THANH TOÁN NGAY</TopButton>
        </Top>
        <Bottom>
          <Info>
            {cart.map((item) => (
              <div key={item.skuId}>
                <Product>
                  <ProductDetail>
                    <Image src={item.img} />
                    <Details>
                      <ProductName>
                        <b>Product:</b> {item.name}
                      </ProductName>
                      <ProductId>
                        <b>SKU:</b> {item.skuId}
                      </ProductId>

                      {item.color && (
                        <ProductColor>
                          <b>Color:</b> {item.color}
                        </ProductColor>
                      )}

                      {item.size && (
                        <ProductSize>
                          <b>Size:</b> {item.size}
                        </ProductSize>
                      )}
                    </Details>
                  </ProductDetail>
                  <PriceDetail>
                    <ProductAmountContainer>
                      <IconButton
                        onClick={() =>
                          handleAmount(item.skuId, item.amount + 1)
                        }
                      >
                        <Add />
                      </IconButton>
                      <ProductAmount>{item.amount}</ProductAmount>
                      <IconButton
                        onClick={() =>
                          handleAmount(item.skuId, item.amount - 1)
                        }
                      >
                        <Remove />
                      </IconButton>
                    </ProductAmountContainer>
                    <ProductPrice>
                      {(item.basePrice * item.amount).toLocaleString()}đ
                    </ProductPrice>
                  </PriceDetail>

                  <CloseButton>
                    <IconButton onClick={() => handleRemove(item.skuId)}>
                      <Close />
                    </IconButton>
                  </CloseButton>
                </Product>
                <Hr />
              </div>
            ))}
          </Info>
          <Summary>
            <SummaryTitle>TỔNG ĐƠN HÀNG</SummaryTitle>
            <SummaryItem>
              <SummaryItemText>Tạm tính</SummaryItemText>
              <SummaryItemPrice>{total.toLocaleString()}đ</SummaryItemPrice>
            </SummaryItem>
            <SummaryItem>
              <SummaryItemText>Phí vận chuyển</SummaryItemText>
              <SummaryItemPrice>{shipCost.toLocaleString()}đ</SummaryItemPrice>
            </SummaryItem>
            <SummaryItem type='total'>
              <SummaryItemText>Tổng cộng</SummaryItemText>
              <SummaryItemPrice>
                {(total + shipCost).toLocaleString()}đ
              </SummaryItemPrice>
            </SummaryItem>
            <Button type='filled'>THANH TOÁN NGAY</Button>
            <Box mt={2}>
              <Button>TIẾP TỤC MUA SẮM</Button>
            </Box>
          </Summary>
        </Bottom>
      </Wrapper>
      <Footer />
    </Container>
  );
};

export default Cart;
