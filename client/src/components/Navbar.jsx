import { Badge } from '@material-ui/core';
import { Search, ShoppingCartOutlined } from '@material-ui/icons';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router';
import styled from 'styled-components';
import { changeValue } from '../features/searchSlice';
import { mobile } from '../responsive';

const Container = styled.div`
  height: 60px;
  ${mobile({ height: '50px' })}
`;

const Wrapper = styled.div`
  padding: 10px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  ${mobile({ padding: '10px 0px' })}
`;

const Left = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
`;

const Language = styled.span`
  font-size: 14px;
  cursor: pointer;
  ${mobile({ display: 'none' })}
`;

const SearchContainer = styled.form`
  border: 0.5px solid lightgray;
  display: flex;
  align-items: center;
  margin-left: 25px;
  padding: 5px;
`;

const Input = styled.input`
  border: none;
  ${mobile({ width: '50px' })}
`;

// const Center = styled.div`
//   flex: 1;
//   text-align: center;
// `;

const Logo = styled.h1`
  font-weight: bold;
  ${mobile({ fontSize: '24px' })}
  cursor: pointer;
`;

const Right = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  ${mobile({ flex: 2, justifyContent: 'center' })}
`;

const MenuItem = styled.div`
  font-size: 14px;
  cursor: pointer;
  margin-left: 25px;
  ${mobile({ fontSize: '12px', marginLeft: '10px' })}
`;

const Navbar = () => {
  const navigate = useNavigate();
  const cartLength = useSelector((state) => state.cart.length);
  const value = useSelector((state) => state.search.value);
  const dispatch = useDispatch();

  const handleChange = (e) => {
    dispatch(changeValue({ keyword: e.target.value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    navigate(`/product-list?keyword=${value}`);
  };

  return (
    <Container>
      <Wrapper>
        <Left>
          <Language>VI</Language>
          <SearchContainer onSubmit={handleSubmit}>
            <Input
              required
              autocomplete='off'
              placeholder='Tìm kiếm'
              value={value}
              onChange={handleChange}
            />
            <Search style={{ color: 'gray', fontSize: 16 }} />
          </SearchContainer>
        </Left>
        {/* <Center> */}
        <Logo onClick={() => navigate('/')}>ÉTOÉT.</Logo>
        {/* </Center> */}
        <Right>
          <MenuItem onClick={() => navigate('/register')}>ĐĂNG KÝ</MenuItem>
          <MenuItem onClick={() => navigate('/login')}>ĐĂNG NHẬP</MenuItem>
          <MenuItem onClick={() => navigate('/cart')}>
            <Badge badgeContent={cartLength} color='primary'>
              <ShoppingCartOutlined />
            </Badge>
          </MenuItem>
        </Right>
      </Wrapper>
    </Container>
  );
};

export default Navbar;
