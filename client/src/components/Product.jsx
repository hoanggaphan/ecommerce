import {
  FavoriteBorderOutlined,
  SearchOutlined,
  ShoppingCartOutlined,
} from '@material-ui/icons';
import { useNavigate } from 'react-router';
import styled from 'styled-components';

const Info = styled.div`
  opacity: 0;
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.2);
  z-index: 3;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.5s ease;
  cursor: pointer;
`;

const Container = styled.div`
  width: 280px;
  position: relative;

  &:hover ${Info} {
    opacity: 1;
  }
`;

const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const Icon = styled.div`
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 10px;
  transition: all 0.5s ease;
  &:hover {
    background-color: #e9f5f5;
    transform: scale(1.1);
  }
`;

const ContainerImg = styled.div`
  height: 361px;
  max-width: 100%;
  background-color: #f5fbfd;
`;

const Name = styled.p`
  text-align: center;
  margin-top: 5px;
  font-size: 18px;
`;

const Product = ({ item }) => {
  const navigate = useNavigate();

  return (
    <div>
      <Container>
        <ContainerImg>
          <Image src={item.img} height={361} />
        </ContainerImg>
        <Info>
          <Icon>
            <ShoppingCartOutlined />
          </Icon>
          <Icon onClick={() => navigate(`/product/${item.slug}`)}>
            <SearchOutlined />
          </Icon>
          <Icon>
            <FavoriteBorderOutlined />
          </Icon>
        </Info>
        <Name>{item.name}</Name>
      </Container>
    </div>
  );
};

export default Product;
