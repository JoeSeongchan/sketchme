/* eslint-disable comma-dangle */
/* eslint-disable no-param-reassign */
/* eslint-disable no-console */
import axios from 'axios';

export const URL = 'https://sketchme.ddns.net';

// 토큰 갱신 함수
const tokenRefresh = async () => {
  try {
    const refreshToken = sessionStorage.getItem('refresh_token');

    if (refreshToken) {
      const response = await axios.post(
        `${URL}/api/token/regenerate-token`,
        {},
        {
          headers: {
            Authorization: `Bearer ${refreshToken}`,
          },
        }
      );

      const newAccessToken = response.data.access_token;
      sessionStorage.setItem('access_token', newAccessToken);
      return newAccessToken;
    }
    return null; // eslint 오류 해결을 위한 null값 반환
  } catch (error) {
    console.error('토큰 교체 실패:', error);
    throw error; // 에러 다시 던지기
  }
};

const api = axios.create({
  baseURL: URL,
});

// 요청 시 인터셉트 - access 토큰이 있을 경우 요청에 넣어서 보낸다.
api.interceptors.request.use(
  async (config) => {
    const accessToken = sessionStorage.getItem('access_token');

    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error) => {
    console.error('axios 요청 실패-api.jsx에서 보냄', error);
    return Promise.reject(error);
  }
);

//  응답시 인터셉트 - 만료시 갱신 후 시도
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response && error.response.data.customCode === 'E3003') {
      // 수정: customCode 접근
      try {
        const newAccessToken = await tokenRefresh();

        // 만료된 토큰을 새 토큰으로 교체하여 재시도
        error.config.headers.Authorization = `Bearer ${newAccessToken}`;
        const response = await axios.request(error.config);
        return response;
      } catch (refreshError) {
        console.error('토큰 만료 및 교체 실패', refreshError);
        return Promise.reject(error);
      }
    }

    return Promise.reject(error);
  }
);

export default api;
