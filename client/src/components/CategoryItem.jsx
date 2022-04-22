import styled from 'styled-components';
import { mobile } from '../responsive';
import { useNavigate } from 'react-router';

const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
  ${mobile({ height: '20vh' })}
  transition: all .5s;
`;

const Container = styled.div`
  flex: 1;
  margin: 3px;
  height: 70vh;
  position: relative;
  overflow: hidden;

  &:hover ${Image} {
    transform: scale(1.2);
  }
`;

const Info = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const Title = styled.h1`
  color: white;
  margin-bottom: 20px;
`;

const Button = styled.button`
  border: none;
  padding: 10px;
  background-color: white;
  color: gray;
  cursor: pointer;
  font-weight: 600;
`;

const CategoryItem = ({ item }) => {
  const navigate = useNavigate();

  return (
    <Container>
      <Image src={item.img} />
      <Info>
        <Title>{item.name}</Title>
        <Button onClick={() => navigate(`/category/${item.slug}`)}>
          SHOP NOW
        </Button>
      </Info>
    </Container>
  );
};

export default CategoryItem;
