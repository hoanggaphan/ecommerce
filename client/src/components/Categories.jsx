import styled from 'styled-components';
import useSWR from 'swr';
import { mobile } from '../responsive';
import CategoryItem from './CategoryItem';

const Container = styled.div`
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  padding: 20px;
  justify-content: space-between;
  ${mobile({ padding: '0px', flexDirection: 'column' })}
`;

const fetchList = (url) => fetch(url).then((r) => r.json());

const Categories = () => {
  const { data } = useSWR('http://localhost:8080/api/v1/categories', fetchList);

  return (
    <Container>
      {data?.map((item) => (
        <CategoryItem item={item} key={item.slug} />
      ))}
    </Container>
  );
};

export default Categories;
