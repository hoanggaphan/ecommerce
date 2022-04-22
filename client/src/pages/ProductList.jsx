import { Box } from '@material-ui/core';
import Pagination from '@material-ui/lab/Pagination';
import React from 'react';
import styled from 'styled-components';
import useSWR from 'swr';
import Announcement from '../components/Announcement';
import Footer from '../components/Footer';
import Navbar from '../components/Navbar';
import Newsletter from '../components/Newsletter';
import Products from '../components/Products';
import useQuery from '../hook/useQuery';
// import { mobile } from '../responsive';

const Container = styled.div``;

const Title = styled.h1`
  margin: 20px;
`;

// const FilterContainer = styled.div`
//   display: flex;
//   justify-content: space-between;
// `;

// const Filter = styled.div`
//   margin: 20px;
//   ${mobile({ width: '0px 20px', display: 'flex', flexDirection: 'column' })}
// `;

// const FilterText = styled.span`
//   font-size: 20px;
//   font-weight: 600;
//   margin-right: 20px;
//   ${mobile({ marginRight: '0px' })}
// `;

// const Select = styled.select`
//   padding: 10px;
//   margin-right: 20px;
//   ${mobile({ margin: '10px 0px' })}
// `;
// const Option = styled.option``;

const Center = styled.div`
  margin-top: 15px;
  display: flex;
  justify-content: center;
`;

const fetchList = (url) => fetch(url).then((r) => r.json());

const ProductList = () => {
  const pageNumber = parseInt(useQuery().get('page')) || 1; // bar
  const length = useQuery().get('length') || ''; // bar
  const keyword = useQuery().get('keyword') || ''; // bar
  const [page, setPage] = React.useState(pageNumber);

  const { data } = useSWR(
    `http://localhost:8080/api/v1/products?page=${page}&length=${length}&keyword=${keyword}`,
    fetchList
  );

  const products = data?.products?.map((i) => ({
    slug: i.slug,
    name: i.name,
    img: i.images[0].src,
  }));

  const handlePagination = (e, value) => setPage(value);

  return (
    <Container>
      <Navbar />
      <Announcement />
      <Title>Có tất cả {data && data?.totalItems} sản phẩm</Title>
      {/* <FilterContainer>
        <Filter>
          <FilterText>Lọc sản phẩm:</FilterText>
          <Select defaultValue='title'>
            <Option disabled value='title'>
              Color
            </Option>
            <Option>White</Option>
            <Option>Black</Option>
            <Option>Red</Option>
            <Option>Blue</Option>
            <Option>Yellow</Option>
            <Option>Green</Option>
          </Select>
          <Select defaultValue='title'>
            <Option disabled value='title'>
              Size
            </Option>
            <Option>XS</Option>
            <Option>S</Option>
            <Option>M</Option>
            <Option>L</Option>
            <Option>XL</Option>
          </Select>
        </Filter>
        <Filter>
          <FilterText>Sort Products:</FilterText>
          <Select>
            <Option>Newest</Option>
            <Option>Price (asc)</Option>
            <Option>Price (desc)</Option>
          </Select>
        </Filter>
      </FilterContainer> */}
      <Products products={products} />
      <Box px='20px'>
        {data && keyword && data?.totalItems === 0 && (
          <p>
            Không tìm thấy "<b>{keyword}</b>". Vui lòng kiểm tra chính tả hoặc
            tìm với từ khóa khác!
          </p>
        )}
      </Box>
      <Center>
        {data && data?.totalPages > 1 && (
          <Pagination
            size='large'
            defaultPage={1}
            page={page}
            onChange={handlePagination}
            count={data?.totalPages}
            shape='rounded'
          />
        )}
      </Center>

      <Newsletter />
      <Footer />
    </Container>
  );
};

export default ProductList;
