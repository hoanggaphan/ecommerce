import { Box } from '@material-ui/core';
import Skeleton from '@material-ui/lab/Skeleton';
import styled from 'styled-components';
import Product from './Product';

const Container = styled.div`
  padding: 20px;
  display: flex;
  flex-wrap: wrap;
  column-gap: 15px;
  row-gap: 25px;
  justify-content: ${(props) => props.justifyContent || 'flex-start'};
`;

const Products = ({ products, justifyContent }) => {
  
  return (
    <Container justifyContent={justifyContent}>
      {products
        ? products?.map((item) => <Product item={item} key={item.slug} />)
        : [...new Array(15)].map((_, i) => (
            <Box key={i} width={280}>
              <Skeleton variant='rect' width='100%' height={350} />
              <Skeleton />
              <Skeleton width='60%' />
            </Box>
          ))}
    </Container>
  );
};

export default Products;
