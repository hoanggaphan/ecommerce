import { useParams } from 'react-router-dom';
import styled from 'styled-components';
import useSWR from 'swr';
import Announcement from '../components/Announcement';
import Footer from '../components/Footer';
import Navbar from '../components/Navbar';
import Newsletter from '../components/Newsletter';
import Products from '../components/Products';

const Container = styled.div``;

const Title = styled.h1`
  margin: 20px;
  text-transform: uppercase;
`;

const fetchList = (url) => fetch(url).then((r) => r.json());

const Category = () => {
  const { slug } = useParams();

  const { data } = useSWR(
    `http://localhost:8080/api/v1/categories/${slug}/products`,
    fetchList
  );

  const products = data?.map((i) => ({
    slug: i.slug,
    name: i.name,
    img: i.images[0].src,
  }));

  return (
    <Container>
      <Navbar />
      <Announcement />
      <Title>{slug}</Title>
      <Products products={products} />

      <Newsletter />
      <Footer />
    </Container>
  );
};

export default Category;
