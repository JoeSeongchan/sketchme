import { useEffect, useState } from 'react';
import API from './api';

function cardHook(endpoint, keyword, orderBy) {
  const [Data, setData] = useState([]);
  const [Loading, setLoading] = useState(true);
  const [Error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await API.get(endpoint);
        const rawData = response.data.data;

        // 가공 로직
        const processedData = rawData.map((item) => {
          if (endpoint.includes('/api/search/drawing')) {
            // 그림 데이터에 대한 가공 로직
            const processedItem = {
              ...item,
              cardUrl: item.pictureImgUrl.imgUrl,
              writerUrl: item.writerImgUrl.thumbnailUrl,
              thumbnailUrl: item.pictureImgUrl.thumbnailUrl,
            };
            return processedItem;
          } if (endpoint.includes('/api/search/artist')) {
            // 작가 데이터에 대한 가공 로직
            const processedItem = {
              ...item,
              cardUrl: item.imgUrlResponse.imgUrl,
              artistID: item.id,
              thumbnailUrl: item.imgUrlResponse.thumbnailUrl,
            };
            return processedItem;
          }
          // 기타 경우에 대한 로직 추가 가능

          // 예외 처리: endpoint가 일치하지 않는 경우 원본 데이터 그대로 반환
          return item;
        });
        setData(processedData);
        setLoading(false);
      } catch (error) {
        setError(error);
        setLoading(false);
        // eslint-disable-next-line no-console
        console.error('cardHook에서 API 요청 에러:', error);
      }
    };

    fetchData();
  }, [endpoint, keyword, orderBy]);

  return { Data, Loading, Error };
}

export default cardHook;
