import { Add, Remove } from '@material-ui/icons';
import React from 'react';
import { useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import TopBarProgress from 'react-topbar-progress-indicator';
import styled from 'styled-components';
import useSWR from 'swr';
import Announcement from '../components/Announcement';
import Footer from '../components/Footer';
import Navbar from '../components/Navbar';
import Newsletter from '../components/Newsletter';
import { addToCart } from '../features/cartSlice';
import { mobile } from '../responsive';

const Container = styled.div``;

const Wrapper = styled.div`
  padding: 50px;
  display: flex;
  ${mobile({ padding: '10px', flexDirection: 'column' })}
`;

const ImgContainer = styled.div`
  flex: 1;
`;

const Image = styled.img`
  width: 100%;
  height: 500px;
  object-fit: cover;
  ${mobile({ height: '40vh' })}
`;

const InfoContainer = styled.div`
  flex: 1;
  padding: 0px 50px;
  ${mobile({ padding: '10px' })}
`;

const Title = styled.h1`
  font-weight: 200;
`;

const Desc = styled.p`
  margin: 20px 0px;
`;

const Price = styled.span`
  font-weight: 100;
  font-size: 40px;
`;

const FilterContainer = styled.div`
  width: 50%;
  margin: 30px 0px;
  display: flex;
  row-gap: 15px;
  flex-wrap: wrap;
  justify-content: space-between;
  ${mobile({ width: '100%' })}
`;

const Filter = styled.div`
  display: flex;
  align-items: center;
`;

const FilterTitle = styled.span`
  font-size: 20px;
  font-weight: 200;
  margin-right: 7px;
`;

// const FilterColor = styled.div`
//   width: 20px;
//   height: 20px;
//   border-radius: 50%;
//   background-color: ${(props) => props.color};
//   margin: 0px 5px;
//   cursor: pointer;
// `;

const FilterSize = styled.select`
  margin-left: 10px;
  padding: 5px;
`;

const FilterSizeOption = styled.option``;

const AddContainer = styled.div`
  width: 50%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  ${mobile({ width: '100%' })}
`;

const AmountContainer = styled.div`
  display: flex;
  align-items: center;
  font-weight: 700;
`;

const Amount = styled.span`
  width: 30px;
  height: 30px;
  border-radius: 10px;
  border: 1px solid teal;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0px 5px;
`;

const Button = styled.button`
  padding: 15px;
  border: 2px solid teal;
  background-color: white;
  cursor: pointer;
  font-weight: 500;

  &:hover {
    background-color: #f8f4f4;
  }
`;

const ImageSmallContainer = styled.div`
  display: flex;
  margin-top: 5px;
  gap: 10px;
`;

const ImageSmall = styled.img`
  cursor: pointer;
  height: 200px;
  max-width: 100%;
`;

const SelectColor = styled.div`
  padding: 2px;
  border: 1px solid black;
  border-radius: 4px;
  margin-right: 2px;
  margin-left: 2px;
  cursor: pointer;
`;

const SkuId = styled.p`
  margin-top: 5px;
  font-size: 14px;
`;

const fetchList = (url) => fetch(url).then((r) => r.json());

const Product = () => {
  const dispatch = useDispatch();

  const { slug } = useParams();

  const { data } = useSWR(
    `http://localhost:8080/api/v1/products/${slug}`,
    fetchList
  );

  const [imgHold, setImgHold] = React.useState();
  const [imgs, setImgs] = React.useState([]);
  const [colors, setColors] = React.useState([]);
  const [sizes, setSizes] = React.useState([]);
  const [amount, setAmount] = React.useState(1);

  const [product, setProduct] = React.useState();

  const [selected, setSelected] = React.useState();

  React.useEffect(() => {
    if (data) {
      setImgHold(data.images[0].src);

      const newData = data.skus.map((sku) => ({
        skuId: sku.id,
        name: data.name,
        img: data.images[0].src,
        basePrice: sku.basePrice,
        qty: sku.qty,
        size: sku.variants.find((item) => item.id.attributeId === 1)?.value,
        color: sku.variants.find((item) => item.id.attributeId === 2)?.value,
      }));

      setProduct(newData);

      let newSizes = newData.map((item) => item.size);
      setSizes([...new Set(newSizes)]);

      const newColors = newData.map((item) => item.color);
      setColors([...new Set(newColors)]);

      setSelected(newData[0]);

      const images = data.images.map((i) => i.src);
      setImgs(images);
    }
  }, [data]);

  if (!data) return <TopBarProgress />;

  const handleSelectSize = (e) => {
    const size = e.target.value;
    const newSelected = product.find(
      (item) => item.size === size && item.color === selected.color
    );
    setSelected(newSelected);
  };

  const handleSelectColor = (color) => {
    const newSelected = product.find(
      (item) => item.color === color && item.size === selected.size
    );
    setSelected(newSelected);
  };

  const handleAddItemToCart = () => {
    const cart = { ...selected, amount };
    delete cart.qty;
    dispatch(addToCart(cart));
  };

  return (
    <Container>
      <Navbar />
      <Announcement />
      <Wrapper>
        <ImgContainer>
          <Image src={imgHold} />
          <ImageSmallContainer>
            {imgs?.map((i) => (
              <ImageSmall
                onClick={() => setImgHold(i)}
                src={i}
                key={i}
                alt=''
              />
            ))}
          </ImageSmallContainer>
        </ImgContainer>
        <InfoContainer>
          <Title>{data.name}</Title>
          <SkuId>SKU: {selected?.skuId}</SkuId>
          <Desc>{data.description}</Desc>
          <Price>{selected?.basePrice?.toLocaleString()}</Price>
          <span style={{ fontSize: '20px', fontWeight: 'lighter' }}> đ</span>
          <FilterContainer>
            {selected && selected.color && (
              <Filter>
                <>
                  <FilterTitle>Màu</FilterTitle>
                  {colors.map((i) => (
                    <SelectColor
                      style={{
                        backgroundColor:
                          selected.color === i ? 'black' : 'transparent',
                        color: selected.color === i ? 'white' : 'black',
                      }}
                      onClick={() => handleSelectColor(i)}
                      key={i}
                    >
                      {i}
                    </SelectColor>
                  ))}
                </>
                {/* <FilterColor color='black' />
              <FilterColor color='darkblue' />
            <FilterColor color='gray' /> */}
              </Filter>
            )}

            {selected && selected.size && (
              <Filter>
                <FilterTitle>Size</FilterTitle>
                <FilterSize onChange={handleSelectSize} value={selected?.size}>
                  {sizes.map((i) => (
                    <FilterSizeOption value={i} key={i}>
                      {i}
                    </FilterSizeOption>
                  ))}
                </FilterSize>
              </Filter>
            )}
          </FilterContainer>
          <AddContainer>
            <AmountContainer>
              <span
                style={{ cursor: 'pointer' }}
                onClick={() => amount > 1 && setAmount(amount - 1)}
              >
                <Remove />
              </span>
              <Amount>{amount}</Amount>
              <span
                style={{ cursor: 'pointer' }}
                onClick={() => amount < 99 && setAmount(amount + 1)}
              >
                <Add />
              </span>
            </AmountContainer>
            <Button onClick={handleAddItemToCart}>THÊM VÀO GIỎ</Button>
          </AddContainer>
        </InfoContainer>
      </Wrapper>
      <Newsletter />
      <Footer />
    </Container>
  );
};

export default Product;
